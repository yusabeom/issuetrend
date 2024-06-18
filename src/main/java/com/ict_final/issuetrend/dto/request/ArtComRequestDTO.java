package com.ict_final.issuetrend.dto.request;

import lombok.*;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtComRequestDTO {
    private Long userNo;
    private String articleCode;
    private String text;
}
