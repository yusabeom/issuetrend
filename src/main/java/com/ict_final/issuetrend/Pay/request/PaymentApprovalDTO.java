package com.ict_final.issuetrend.Pay.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentApprovalDTO {
    private Long userNo;
    private String pgToken;
    private String tid;
}
