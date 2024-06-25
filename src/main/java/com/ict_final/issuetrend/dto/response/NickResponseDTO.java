package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.User;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NickResponseDTO {
    private Long userNo;
    private String nickname;
    private String email;
    private String regionName;

    public NickResponseDTO(User user) {
        this.userNo = user.getUserNo();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.regionName = user.getRegionName();
    }
}
