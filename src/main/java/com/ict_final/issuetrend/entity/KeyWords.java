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
@Table(name = "tbl_keywords")
public class KeyWords {

    //기사 코드
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_code")
    private Article article;

    //키워드
    @Column(length = 50)
    private String keyword;

}
