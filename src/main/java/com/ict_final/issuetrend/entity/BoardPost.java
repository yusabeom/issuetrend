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
@Table(name = "tbl_board_post")
public class BoardPost {

    //게시글번호
    @Id
    @Column(name = "post_no")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String postNo;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //내용
    @Column(length = 10000)
    private String text;

    //작성날짜
    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;

    //사진
    @Column(length = 2500)
    private String img;
}
