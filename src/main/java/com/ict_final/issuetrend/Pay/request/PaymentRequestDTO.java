package com.ict_final.issuetrend.Pay.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long userNo;
    private String itemName;
    private int quantity;
    private int totalAmount;
}