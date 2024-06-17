package com.ict_final.issuetrend.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeywordsFrequencyResponseDTO {

    private String keyword;
    private int frequency;

}
