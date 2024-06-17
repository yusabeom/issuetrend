package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.Article;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

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
    private String truncatedText; // 본문 앞부분 자른 내용

    public ArticleDetailResponseDTO(Article article) {
        this.articleCode = article.getArticleCode();
        this.title = article.getTitle();
        this.text = article.getText();
        this.createdDate = article.getCreatedDate();
        this.newsAgency = article.getNewsAgency();
        this.writer = article.getWriter();
        this.img = article.getImg();
        this.articleLink = article.getArticleLink();
        this.truncatedText = truncateText(article.getText(), 100);
    }

    // 한글 바이트 깨짐 방지 100바이트로 자르는 메서드
    private String truncateText(String text, int maxLength) {
        if (text != null && text.length() > maxLength) {
            // maxLength 길이를 초과할 때만 자르기
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : text.toCharArray()) {
                // 각 문자의 바이트 길이 확인
                byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                if (currentLength + bytes.length > maxLength) {
                    break;
                }
                sb.append(c);
                currentLength += bytes.length;
            }
            return sb.toString() + (currentLength < text.length() ? "..." : "");
        }
        // 본문이 100바이트를 넘지 않는다면 기존 text를 리턴
        return text;
    }

}
