package com.ict_final.issuetrend.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class NaverRestResponseDto {

    private final String title;
    private final String link;
    private final String category;
    private final String description;
    private final String telephone;
    private final String address;
    private final String roadAddress;
    private final long mapx;
    private final long mapy;
}
