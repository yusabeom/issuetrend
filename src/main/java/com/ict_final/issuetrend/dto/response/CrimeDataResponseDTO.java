package com.ict_final.issuetrend.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrimeDataResponseDTO {

    String category;
    Long frequency;

}
