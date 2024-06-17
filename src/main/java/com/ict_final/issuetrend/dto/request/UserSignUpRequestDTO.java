package com.ict_final.issuetrend.dto.request;

import com.ict_final.issuetrend.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequestDTO {
    @NotBlank
    @Size(max = 32)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String password;

    @NotBlank
    private String regionName;

    public User toEntity(String uploadedFilePath) {
        return User.builder()
                .email(email)
                .password(password)
                .regionName(regionName)
                .profileImage(uploadedFilePath)
                .build();
    }
}
