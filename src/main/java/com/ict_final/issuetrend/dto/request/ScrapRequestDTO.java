package com.ict_final.issuetrend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapRequestDTO {
    @NotBlank
    private Long userNo;

    @NotBlank
    private String articleCode;
}
