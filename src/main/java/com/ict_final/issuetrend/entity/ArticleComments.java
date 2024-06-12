package com.ict_final.issuetrend.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_article_comments")
public class ArticleComments {
    //댓글 번호
    @Id
    @Column(name = "comment_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentNo;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //기사코드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_code")
    private Article article;

    //내용
    @Column(name = "text", length = 500)
    private String text;

}
