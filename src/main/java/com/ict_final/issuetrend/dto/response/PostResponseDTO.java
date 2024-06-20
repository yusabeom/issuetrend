package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.BoardPost;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {


    private Long postNo;
    private Long userNo;
    private String title;
    private String text;
    private LocalDateTime writeDate;
    private String img;
    private String email;
    private String formatDate;

    public PostResponseDTO(BoardPost boardPost) {
        this.postNo = boardPost.getPostNo();
        this.userNo = boardPost.getUser().getUserNo();
        this.title = boardPost.getTitle();
        this.text = boardPost.getText();
        this.writeDate = boardPost.getWriteDate();
        this.img = boardPost.getImg();
        this.email = boardPost.getUser().getEmail();
        this.formatDate = formatCreatedDate(boardPost.getWriteDate());
    }
}
