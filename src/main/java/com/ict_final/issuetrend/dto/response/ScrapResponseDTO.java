package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.Scrap;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapResponseDTO {

    private String articleCode;
    private Long scrapNo;

    public ScrapResponseDTO(Scrap scrap) {
        this.articleCode = scrap.getArticle().getArticleCode();
        this.scrapNo = scrap.getScrapNo();
    }
}
