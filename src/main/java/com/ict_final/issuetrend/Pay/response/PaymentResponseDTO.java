package com.ict_final.issuetrend.Pay.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String tid;
    private String nextRedirectPcUrl;
}
