package com.ict_final.issuetrend.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_scrap")
public class Scrap {

    //스크랩 번호
    @Id
    @Column(name = "scrap_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapNo;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //게시글 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_code")
    private Article article;

}
