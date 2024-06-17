package com.ict_final.issuetrend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_user")
public class User {
    //회원번호
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ArticleComments> articleComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<BoardPost> boardPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostComments> postComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<LocalChat> localChats;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<FavoriteKeyword> favoriteKeywords;
}
