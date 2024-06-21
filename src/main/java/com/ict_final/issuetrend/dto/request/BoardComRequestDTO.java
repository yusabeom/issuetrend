package com.ict_final.issuetrend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardComRequestDTO {
    @NotBlank
    private Long userNo;

    @NotBlank
    private Long postNo;

    @NotBlank
    @Size(max = 500)
    private String text;
}
