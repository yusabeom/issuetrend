package com.ict_final.issuetrend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ict_final.issuetrend.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

// 로그인 성공 후 클라이언트에게 전송할 데이터 객체
@Getter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class KakaoLoginResponseDTO {

    private String email;
    private String profileImage;
    private Map<String, String> token; // 인증 토큰


    public KakaoLoginResponseDTO(User user, Map<String, String> token) {
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.token = token;

    }

}
