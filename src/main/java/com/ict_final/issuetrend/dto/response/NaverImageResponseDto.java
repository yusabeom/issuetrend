package com.ict_final.issuetrend.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class NaverImageResponseDto {
    private final String title;
    private final String link;
    private final String thumbnail;
    private final String sizeheight;
    private final String sizewidth;
}
