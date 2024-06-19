package com.ict_final.issuetrend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDTO {
    @NotBlank
    private Long userNo;

    @NotBlank
    @Size(max = 10000)
    private String text;

    private String img;
}
