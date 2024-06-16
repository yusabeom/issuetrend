package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.auth.TokenProvider;
import com.ict_final.issuetrend.auth.TokenUserInfo;
import com.ict_final.issuetrend.dto.request.LoginRequestDTO;
import com.ict_final.issuetrend.dto.request.UserSignUpRequestDTO;
import com.ict_final.issuetrend.dto.response.KakaoLoginResponseDTO;
import com.ict_final.issuetrend.dto.response.KakaoUserDTO;
import com.ict_final.issuetrend.dto.response.LoginResponseDTO;
import com.ict_final.issuetrend.dto.response.UserSignUpResponseDTO;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${kakao.client_id}")
    private String KAKAO_CLIENT_ID;
    @Value("${kakao.redirect_url}")
    private String KAKAO_REDIRECT_URL;
    @Value("${kakao.client_secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${upload.path}")
    private String uploadRootPath;

    //   email 중복 확인 처리
    public boolean isDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다. - {}", email);
            return true;
        } else return false;
    }

    public String uploadProfileImage(MultipartFile profileImage) throws IOException {
        // 루트 디렉토리가 실존하는 지 확인 후 존재하지 않으면 생성.
        File rootDir = new File(uploadRootPath);
        if (!rootDir.exists()) rootDir.mkdirs();
        // 파일명을 유니크하게 변경 (이름 충돌 가능성을 대비)
        // UUID와 원본파일명을 결합
        String uniqueFileName
                = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
        // 파일 저장
        File uploadFile = new File(uploadRootPath + "/" + uniqueFileName);
        profileImage.transferTo(uploadFile);
        return uniqueFileName;
    }

    public UserSignUpResponseDTO create(
            final UserSignUpRequestDTO dto, final String uploadedFilePath) {
        String email = dto.getEmail();
        if (isDuplicate(email)) {
            throw new RuntimeException("중복된 이메일 입니다.");
        }
        // 패스워드 인코딩
        String encoded = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encoded);
        // dto를 User Entity로 변환해서 저장.
        User saved = userRepository.save(dto.toEntity(uploadedFilePath));
        log.info("회원 가입 정상 수행됨! - saved user - {}", saved);
        return new UserSignUpResponseDTO(saved);
    }

    public LoginResponseDTO login(final LoginRequestDTO dto) {
        // 회원 정보 조회 (이메일)
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));

        // 패스워드 검증
        String rawPassword = dto.getPassword(); // 입력한 비번
        String encodedPassword = user.getPassword(); // DB에 저장된 암호화된 비번
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        log.info("{}님 로그인 성공!", user.getEmail());
        Map<String, String> token = getTokenMap(user);
        user.changeRefreshToken(token.get("refresh_token"));
        user.changeRefreshExpiryDate(tokenProvider.getExpiryDate(token.get("refresh_token")));
        userRepository.save(user);
        return new LoginResponseDTO(user, token);
    }
    private Map<String, String> getTokenMap(User user) {
        String accessToken = tokenProvider.createAccessKey(user);
        String refreshToken = tokenProvider.createRefreshKey(user);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", accessToken);
        token.put("refresh_token", refreshToken);
        return token;
    }

    public KakaoLoginResponseDTO kakaoService(String code) {
        // 인가 코드를 통해 토큰을 발급받기
        String accessToken = getKakaoAccessToken(code);
        log.info("token: {}", accessToken);

        // 토큰을 통해 사용자 정보를 가져오기
        KakaoUserDTO userDTO = getKakaoUserInfo(accessToken);
        log.info("userDTO: {}", userDTO);

        // 일회성 로그인으로 처리 -> dto를 바로 화면단에 리턴
        // 회원가입 처리 -> 이메일 중복 검사 진행 -> 자체 jwt를 생성해서 토큰을 화면단에 리턴.
        // -> 화면단에서는 적절한 url을 선택하여 redirect를 진행.

        if (!isDuplicate(userDTO.getKakaoAccount().getEmail())) {
            // 이메일이 중복되지 않았다. -> 이전에 로그인 한 적 없음 -> DB에 데이터를 세팅
            User saved = userRepository.save(userDTO.toEntity(accessToken));
        }
        // 이메일이 중복됐다? -> 이전에 로그인 한 적이 있다. -> DB에 데이터를 또 넣을 필요는 없다.
        User foundUser
                = userRepository.findByEmail(userDTO.getKakaoAccount().getEmail()).orElseThrow();

        // 우리 사이트에서 사용하는 jwt를 생성.
        Map<String, String> token = getTokenMap(foundUser);

        // 기존에 로그인했던 사용자의 access token값을 update
        foundUser.changeAccessToken(accessToken);
        userRepository.save(foundUser);
        String profileImageUrl = userDTO.getKakaoAccount().getProfile().getProfileImageUrl();
        return new KakaoLoginResponseDTO(foundUser.getEmail(), profileImageUrl,token);
    }
    private KakaoUserDTO getKakaoUserInfo(String accessToken) {
        // 요청 uri
        String requestURI = "https://kapi.kakao.com/v2/user/me";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        RestTemplate template = new RestTemplate();
        ResponseEntity<KakaoUserDTO> responseEntity
                = template.exchange(requestURI, HttpMethod.GET, new HttpEntity<>(headers), KakaoUserDTO.class);

        // 응답 바디 꺼내기
        KakaoUserDTO responseData = responseEntity.getBody();
        log.info("user profile: {}", responseData);

        return responseData;
    }
    private String getKakaoAccessToken(String code) {

        // 요청 uri
        String requestURI = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디(파라미터) 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 카카오 공식 문서 기준 값으로 세팅
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 디벨로퍼 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URL); // 카카오 디벨로퍼 등록된 redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 전달받은 코드값
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 카카오 디벨로퍼 client secret(활성화 시 추가해 줘야 함)

        // 헤더와 바디 정보를 합치기 위해 HttpEntity 객체 생성
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

        // 카카오 서버로 POST 통신
        RestTemplate template = new RestTemplate();


        ResponseEntity<Map> responseEntity
                = template.exchange(requestURI, HttpMethod.POST, requestEntity, Map.class);


        Map<String, Object> responseData = (Map<String, Object>) responseEntity.getBody();
        log.info("토큰 요청 응답 데이터: {}", responseData);

        // 여러가지 데이터 중 access_token이라는 이름의 데이터를 리턴
        // Object를 String으로 형 변환해서 리턴.
        return (String) responseData.get("access_token");
    }

    public String logout(TokenUserInfo userInfo) {
        User foundUser = userRepository.findById(userInfo.getUserNo())
                .orElseThrow();

        String accessToken = foundUser.getAccessToken();
        if (accessToken != null) {
            String reqURI = "https://kapi.kakao.com/v1/user/logout";
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            ResponseEntity<String> responseData
                    = new RestTemplate().exchange(reqURI, HttpMethod.POST, new HttpEntity<>(headers), String.class);
            foundUser.changeAccessToken(null);
            userRepository.save(foundUser);

            return responseData.getBody();
        }
        return null;
    }
    public String findProfilePath(String userId) {
        User user
                = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
        String profileImg = user.getProfileImage();
        if (profileImg.startsWith("http://")) {
            return profileImg;
        }
        // DB에는 파일명만 저장. -> service가 가지고 있는 Root Path와 연결해서 리턴
        return uploadRootPath + "/" + profileImg;
    }
    public String renewalAccessToken(Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        boolean isValid = tokenProvider.validateRefreshToken(refreshToken);
        if (isValid) {
            // 토큰 값이 유효하다면 만료일자를 검사하자
            User foundUser = userRepository.findByRefreshToken(refreshToken).orElseThrow();
            if (!foundUser.getRefreshTokenExpiryDate().before(new Date())) {
                // 만료일이 오늘보다 이전이 아니라면 -> 만료되지 않았다면
                String newAccessKey = tokenProvider.createAccessKey(foundUser);
                return newAccessKey;
            }
        }

        return null;
    }
}