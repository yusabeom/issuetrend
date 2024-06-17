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
@Table(name = "tbl_favorite_keyword")

public class FavoriteKeyword {

    //키워드 번호
    @Id
    @Column(name = "favorite_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteNo;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //키워드
    @Column(name = "favorite_keyword", length = 20)
    private String favoriteKeyword;
}
