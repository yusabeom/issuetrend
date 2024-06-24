package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.Article;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String shortTitle;
    private String text;
    private LocalDateTime createdDate;
    private String newsAgency;
    private String writer;
    private String img;
    private String articleLink;
    private String truncatedText; // 본문 앞부분 자른 내용
    private String formattedCreatedDate; // 포맷된 날짜 (yyyy년 MM월 dd일)

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일");

    public ArticleDetailResponseDTO(Article article) {
        this.articleCode = article.getArticleCode();
        this.title = article.getTitle();
        this.shortTitle = shortTitle(article.getTitle());
        this.text = article.getText();
        this.createdDate = article.getCreatedDate();
        this.newsAgency = article.getNewsAgency();
        this.writer = article.getWriter();
        this.img = article.getImg();
        this.articleLink = article.getArticleLink();
        this.truncatedText = truncateText(article.getText(), 100);
        this.formattedCreatedDate = formatCreatedDate(article.getCreatedDate());
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

    // createdDate를 포맷하는 메서드
    public static String formatCreatedDate(LocalDateTime createdDate) {
            // 널체크
            if (createdDate == null) {
                return "";
            }

            Duration duration = Duration.between(createdDate, LocalDateTime.now());
            long minutes = duration.toMinutes();
            long hours = duration.toHours();
            long days = duration.toDays();

            // 5분 미만은 방금 전 으로 표시, 7일 이상은 (yyyy년 MM월 dd일) 형식으로 표기
            if (minutes < 5) {
                return "방금 전";
            } else if (minutes < 60) {
                return minutes + "분 전";
            } else if (hours < 24) {
                return hours + "시간 전";
            } else if (days < 7) {
                return days + "일 전";
            } else {
                return createdDate.format(formatter);
            }
    }

    // shortTitle 10글자 미만 메서드
    private String shortTitle(String title) {
        if (title.length() <= 10) {
            return title;
        }
        return title.substring(0, 10) + "...";
    }

}
