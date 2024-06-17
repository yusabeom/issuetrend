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
@Table(name = "tbl_search_term")

public class SearchTerm {

    //검색어 번호
    @Id
    @Column(name = "search_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchNo;

    //검색어
    @Column(name = "search_term", length = 50)
    private String searchTerm;

}
