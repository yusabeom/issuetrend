package com.ict_final.issuetrend.auth;



import lombok.*;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private Long userNo;
    private String email;


}
