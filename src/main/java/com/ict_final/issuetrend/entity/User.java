package com.ict_final.issuetrend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;
@Setter
@Getter
@ToString(exclude = "favoriteKeywords")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_user")
public class User {
    //회원번호
    @Setter
    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    //프로필 사진
    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    //아이디(이메일)
    @Size(max = 32)
    @Email
    @Column(nullable = false, unique = true,  length = 100)
    private String email;

    //비밀번호
    @Column(nullable = false, length = 100)
    private String password;

    //닉네임
    @Column(name = "nickname", length = 20,nullable = false)
    private String nickname;

    //지역명
    @Column(name = "region_name", length = 20,nullable = false)
    private String regionName;

    //로그인경로
    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "login_path", length = 20)
    private LoginPath loginPath = LoginPath.COMMON;

    private String accessToken; // 카카오 로그인 시 발급받는 accessToken을 저장 -> 로그아웃 때 필요

    @Column(length = 400)
    private String refreshToken; // 리프레시 토큰의 값.

    private Date refreshTokenExpiryDate; // 리프레시 토큰의 만료일.

    @Builder.Default
    private boolean isSubscribed = false;

    @Column(length = 100)
    private String tid; // 카카오페이 결제 시 개인에게 부여되는 고유 번호


    // 카카오 access token 저장하는 필드
    public void changeAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void changeRefreshExpiryDate(Date date) {
        this.refreshTokenExpiryDate = date;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ArticleComments> articleComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BoardPost> boardPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostComments> postComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<LocalChat> localChats;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteKeyword> favoriteKeywords;
    public void setFavoriteKeywords(List<FavoriteKeyword> favoriteKeywords) {
        this.favoriteKeywords = favoriteKeywords;
        favoriteKeywords.forEach(fk -> fk.setUser(this));
    }

    // 기존 키워드들을 유지하면서 새로운 키워드 추가
    public void addFavoriteKeywords(List<String> keywords) {
        for (String keyword : keywords) {
            FavoriteKeyword favoriteKeyword = new FavoriteKeyword(keyword, this);
            this.favoriteKeywords.add(favoriteKeyword);
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Scrap> scraps;

    // private boolean isSubscribed = false;

}
