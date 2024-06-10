package com.ict_final.issuetrend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_post_comments")
public class PostComments {

    //댓글 번호
    @Id
    @Column(name = "comment_no")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentNo;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //게시글번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private BoardPost boardPost;

    //내용
    private String text;

    //작성 날짜
    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;
}
