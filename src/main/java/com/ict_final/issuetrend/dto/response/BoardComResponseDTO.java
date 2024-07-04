package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.ArticleComments;
import com.ict_final.issuetrend.entity.PostComments;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardComResponseDTO {
    private Long commentNo;
    private Long userNo;
    private Long postNo;
    private String text;
    private String email;
    private String nickname;
    private String profileImage;

    public BoardComResponseDTO(PostComments comment) {
        this.commentNo = comment.getCommentNo();
        this.userNo = comment.getUser().getUserNo();
        this.postNo = comment.getBoardPost().getPostNo();
        this.text = comment.getText();
        this.email = maskEmail(comment.getUser().getEmail());
        this.nickname = comment.getUser().getNickname();
        this.profileImage = comment.getUser().getProfileImage();
    }

    private String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email; // 유효한 이메일 형식이 아닌 경우
        }
        String username = parts[0];
        /*if (username.length() <= 3) {
            return username + "****";
        }
        String maskedUsername = username.substring(0, 3) + "****";
        return maskedUsername;*/
        return username;
    }
}
