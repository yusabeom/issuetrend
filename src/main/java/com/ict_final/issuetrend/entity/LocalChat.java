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
@Table(name = "tbl_local_chat")
public class LocalChat {
    //지역별 채팅방 코드
    @Id
    @Column(name = "area_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaCode;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private User user;

    //내용
    @Column(nullable = false,length = 300)
    private String text;

    //작성 시간
    @CreationTimestamp
    @Column(name = "write_date")
    private LocalDateTime writeDate;


}
