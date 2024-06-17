package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.KeyWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    // 오늘 기사 모두 가져오기
    @Query("SELECT a FROM Article a " +
            "WHERE FUNCTION('DATE', a.createdDate) = CURRENT_DATE")
    List<Article> findArticleByDate();


    // 지역별 오늘 기사 모두 가져오기
    @Query("SELECT DISTINCT a FROM Article a JOIN a.keywords k " +
            "WHERE k.keyword LIKE :region AND DATE(a.createdDate) = CURRENT_DATE")
    List<Article> findArticleByRegion(@Param("region") String region);

    // 키워드로 검색하기
    @Query("SELECT DISTINCT a FROM Article a WHERE a.text LIKE %:keyword%")
    List<Article> findAtriclesByKeyword(@Param("keyword") String keyword);

    // 기사 코드로 기사 상세 조회
    Article findByArticleCode(String articleCode);
}
