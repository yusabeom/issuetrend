package com.ict_final.issuetrend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ict_final.issuetrend.entity.LoginPath;
import com.ict_final.issuetrend.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String email;
    private LoginPath loginPath;
    private String profileImage;
    private String regionName;
    private String phoneNum;

    public LoginResponseDTO(User user) {
        this.email = user.getEmail();
        this.loginPath = user.getLoginPath();
        this.profileImage = user.getProfileImage();
        this.regionName = user.getRegionName();
        this.phoneNum = user.getPhoneNum();
    }
}
