package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpResponseDTO {
    private String email;
    private String regionName;

    public UserSignUpResponseDTO(User saved) {
        this.email = saved.getEmail();
        this.regionName = saved.getRegionName();
    }
}
