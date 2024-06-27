package com.ict_final.issuetrend.Pay.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class PaymentApprovalResponseDTO {
    private String tid;
    private String itemName;
    private int totalAmount;
    public PaymentApprovalResponseDTO(String tid, String itemName, int totalAmount) {
        this.tid = tid;
        this.itemName = itemName;
        this.totalAmount = totalAmount;
    }
}
