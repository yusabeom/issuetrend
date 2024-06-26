package com.ict_final.issuetrend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtComRequestDTO {
    @NotBlank
    private Long userNo;

    @NotBlank
    private String articleCode;

    @NotBlank
    @Size(max = 500)
    private String text;
}
