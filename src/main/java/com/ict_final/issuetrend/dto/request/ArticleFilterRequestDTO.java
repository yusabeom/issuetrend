package com.ict_final.issuetrend.dto.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleFilterRequestDTO {
    private String region;
    private String newsAgency;
    private String sortOption;  // "최신순" or "댓글순"
    private String keyword;
}
