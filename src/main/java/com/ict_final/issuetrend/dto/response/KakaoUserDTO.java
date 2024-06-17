package com.ict_final.issuetrend.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ict_final.issuetrend.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class KakaoUserDTO {

    private long userNo;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Setter @Getter
    @ToString
    public static class KakaoAccount {

        private String email;
        private Profile profile;

        @Getter @Setter
        @ToString
        public static class Profile {

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
    public User toEntity(String accessToken) {
        return User.builder()
                .email(this.kakaoAccount.email)
                .password("password!")
                .profileImage(this.kakaoAccount.profile.profileImageUrl)
                .accessToken(accessToken)
                .build();
    }

    }





