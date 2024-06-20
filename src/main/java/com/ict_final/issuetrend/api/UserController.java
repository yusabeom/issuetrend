package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.auth.TokenUserInfo;
import com.ict_final.issuetrend.dto.request.LoginRequestDTO;
import com.ict_final.issuetrend.dto.request.UserSignUpRequestDTO;
import com.ict_final.issuetrend.dto.response.KakaoLoginResponseDTO;
import com.ict_final.issuetrend.dto.response.LoginResponseDTO;
import com.ict_final.issuetrend.dto.response.UserSignUpResponseDTO;
import com.ict_final.issuetrend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class UserController {
    private final UserService userService;
    // 이메일 중복 확인 요청 처리
    @GetMapping("/check")
    public ResponseEntity<?> check(String email) {
        log.info("Received email check request for: {}", email);
        if (email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("이메일이 존재하지 않습니다.");
        }
        boolean resultFlag = userService.isDuplicate(email);
        log.info("Email duplication check result: {}", resultFlag);
        return ResponseEntity.ok().body(resultFlag);
    }
    @GetMapping("nick-check")
    public ResponseEntity<?> nickCheck(String nickname){
        if (nickname.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("닉네임이 존재하지 않습니다.");
        }
        boolean nickDuplicate = userService.nickDuplicate(nickname);
        log.info("nick nickDuplicate check result: {}", nickDuplicate);
        return ResponseEntity.ok().body(nickDuplicate);
    }
    // 회원가입 요청 처리
    @PostMapping
    public ResponseEntity<?> signUp(
            @Validated @RequestPart("user") UserSignUpRequestDTO dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            BindingResult result
    ) {
        log.info("/issue-trend POST! - {}", dto);
        ResponseEntity<FieldError> resultEntity = getFieldErrorResponseEntity(result);
        if (resultEntity != null) return resultEntity;
        try {
            String uploadedFilePath = null;
            if (profileImage != null) {
                log.info("attached file name: {}", profileImage.getOriginalFilename());
                // 전달받은 프로필 이미지를 먼저 지정된 경로에 저장한 후 저장 경로를 DB에 세팅하자.
                uploadedFilePath = userService.uploadProfileImage(profileImage);
            }
            UserSignUpResponseDTO responseDTO = userService.create(dto, uploadedFilePath);
            log.info("responseDTO: {}", responseDTO);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IOException e) {
            log.error("An unexpected error occurred!", e);
            e.printStackTrace();
            throw new RuntimeException("An unexpected error occurred!", e);
        }
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Validated @RequestBody LoginRequestDTO dto,
            BindingResult result
    ) {
        log.info("/issue-trend/login POST! - {}", dto);
        ResponseEntity<FieldError> resultEntity2 = getFieldErrorResponseEntity(result);
        if (resultEntity2 != null) return resultEntity2;
        LoginResponseDTO responseDTO = userService.login(dto);
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("/kakaologin")
    public ResponseEntity<?> kakaoLogin(String code) {
        log.info("/api/auth/kakaoLogin - GET! code: {}", code);
        KakaoLoginResponseDTO responseDTO = userService.kakaoService(code);

        return ResponseEntity.ok().body(responseDTO);
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        log.info("/api/auth/logout - GET! - user: {}", userInfo.getEmail());
        String result = userService.logout(userInfo);
        return ResponseEntity.ok().body(result);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> tokenRequest){
        log.info("/api/auth/refresh: POST! - tokenRequest: {}", tokenRequest);
        String renewalAccessToken = userService.renewalAccessToken(tokenRequest);
        if (renewalAccessToken != null) {
            return ResponseEntity.ok().body(Map.of("accessToken", renewalAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
    // 프로필 사진 이미지 데이터를 클라이언트에게 응답 처리
    @GetMapping("/load-profile")
    public ResponseEntity<?> loadFile(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ) {
        log.info("-------userinfo {}", userInfo.getUserNo());

        try {
            // 1. 프로필 사진의 경로부터 얻어야 한다.
            String filePath = userService.findProfilePath(userInfo.getUserNo());
            log.info("filePath: {}", filePath);

            // 2. 얻어낸 파일 경로를 통해 실제 파일 데이터를 로드하기.
            File profileFile = new File(filePath);

            // 모든 사용자가 프로필 사진을 가지는 것은 아니다. -> 프사를 등록하지 않은 사람은 해당 경로가 존재하지 않을 것.
            // 만약 존재하지 않는 경로라면 클라이언트로 404 status를 리턴.
            if (!profileFile.exists()) {
                // 만약 조회한 파일 경로가 http://~~~로 시작한다면 -> 카카오 로그인 한 사람이다!
                // 카카오 로그인 프로필은 변환 과정 없이 바로 이미지 url을 리턴해 주시면 됩니다.
                if (filePath.startsWith("http://")) {
                    return ResponseEntity.ok().body(filePath);
                }
                return ResponseEntity.notFound().build();
            }

            // 해당 경로에 저장된 파일을 바이트 배열로 직렬화 해서 리턴
            byte[] fileData = FileCopyUtils.copyToByteArray(profileFile);

            // 3. 응답 헤더에 컨텐츠 타입을 설정
            HttpHeaders headers = new HttpHeaders();
            MediaType contentType = findExtensionAndGetMediaType(filePath);
            if (contentType == null) {
                return ResponseEntity.internalServerError()
                        .body("발견된 파일은 이미지 파일이 아닙니다.");
            }
            headers.setContentType(contentType);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static MediaType findExtensionAndGetMediaType(String filePath) {

        // 파일 경로에서 확장자 추출
        // C:/todo_upload/nlskdnakscnlknklcs_abc.jpg
        String ext
                = filePath.substring(filePath.lastIndexOf(".") + 1);

        // 추출한 확장자를 바탕으로 MediaType을 설정 -> Header에 들어갈 Content-type이 됨.
        switch (ext.toUpperCase()) {
            case "JPG": case "JPEG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }
    }
    // BindingResult 에서 유효성 검사 오류가 있는지 확인
    private static ResponseEntity<FieldError> getFieldErrorResponseEntity(BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }
        return null;
    }


    @GetMapping("/send-password")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {

        if (userService.isDuplicate(email)) {
            try {
                userService.sendEmail(email);
                return new ResponseEntity<>("Email sent successfully!", HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error sending email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("가입 정보가 없습니다", HttpStatus.BAD_REQUEST);
        }
    }
}
