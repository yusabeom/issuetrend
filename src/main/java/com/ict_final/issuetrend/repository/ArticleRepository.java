package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.KeyWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    // 오늘 기사 모두 가져오기
    @Query("SELECT a FROM Article a " +
            "WHERE FUNCTION('DATE', a.createdDate) = :yesterdayDate")
    List<Article> findArticleByDate(@Param("yesterdayDate") LocalDate yesterdayDate);
//    @Query("SELECT a FROM Article a")
//    List<Article> findArticleByDate();


    // 지역별 오늘 기사 모두 가져오기
    @Query("SELECT DISTINCT a FROM Article a JOIN a.keywords k " +
            "WHERE k.keyword LIKE :region AND DATE(a.createdDate) = :yesterdayDate")
    List<Article> findArticleByRegion(@Param("region") String region, @Param("yesterdayDate") LocalDate yesterdayDate);
//    @Query("SELECT DISTINCT a FROM Article a JOIN a.keywords k WHERE k.keyword LIKE :region")
//    List<Article> findArticleByRegion(@Param("region") String region);

    // 키워드로 검색하기
//    @Query("SELECT DISTINCT a FROM Article a WHERE a.text LIKE %:keyword%")
    @Query("SELECT DISTINCT a FROM Article a JOIN a.keywords k WHERE k.keyword LIKE :keyword")
    List<Article> findArticlesByKeyword(@Param("keyword") String keyword);

    // 기사 코드로 기사 상세 조회
    Article findByArticleCode(String articleCode);

    @Query("SELECT DISTINCT a FROM Article a " +
            "LEFT JOIN a.keywords k " +
            "WHERE " +
            "(:region IS NULL OR k.keyword LIKE %:region%) AND " +
            "(:newsAgency IS NULL OR a.newsAgency LIKE %:newsAgency%) AND " +
            "(:keyword IS NULL OR a.title LIKE %:keyword% OR a.text LIKE %:keyword%) " +
            "ORDER BY " +
            "CASE " +
            "WHEN :sortOption = '최신순' OR :sortOption IS NULL THEN a.createdDate " +
            "WHEN :sortOption = '댓글순' THEN SIZE(a.articleComments) " +
            "END DESC")
    List<Article> findArticlesByFilters(
            @Param("region") String region,
            @Param("newsAgency") String newsAgency,
            @Param("sortOption") String sortOption,
            @Param("keyword") String keyword
    );
}
