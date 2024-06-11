package com.ict_final.issuetrend.auth;

import com.ict_final.issuetrend.entity.LoginPath;
import lombok.*;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String userNo;
    private String email;
    private LoginPath loginPath;

}
