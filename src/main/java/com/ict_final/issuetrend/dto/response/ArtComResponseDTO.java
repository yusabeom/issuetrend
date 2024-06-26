package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.ArticleComments;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtComResponseDTO {
    private Long commentNo;
    private Long userNo;
    private String articleCode;
    private String text;
    private String email;
    private String profileImage;

    public ArtComResponseDTO(ArticleComments comment) {
        this.commentNo = comment.getCommentNo();
        this.userNo = comment.getUser().getUserNo();
        this.articleCode = comment.getArticle().getArticleCode();
        this.text = comment.getText();
        this.email = comment.getUser().getEmail();
        this.profileImage = comment.getUser().getProfileImage();
    }
}
