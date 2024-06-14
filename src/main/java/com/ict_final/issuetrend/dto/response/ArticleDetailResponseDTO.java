package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDetailResponseDTO {

    private String articleCode;
    private String title;
    private String text;
    private LocalDateTime createdDate;
    private String newsAgency;
    private String writer;
    private String img;
    private String articleLink;

    public ArticleDetailResponseDTO(Article article) {
        this.articleCode = article.getArticleCode();
        this.title = article.getTitle();
        this.text = article.getArticleCode();
        this.createdDate = article.getCreatedDate();
        this.newsAgency = article.getNewsAgency();
        this.writer = article.getWriter();
        this.img = article.getImg();
        this.articleLink = article.getArticleLink();
    }
}
