package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.KeyWords;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<KeyWords, Long> {

    // 오늘 기사의 모든 키워드 가져오기
    @Query("SELECT k FROM KeyWords k " +
            "JOIN k.article a " +
            "WHERE FUNCTION('DATE', a.createdDate) = CURRENT_DATE")
    List<KeyWords> findKeyWordsByDate();

    // 지역별 오늘 기사의 모든 키워드 가져오기
    @Query("SELECT k FROM KeyWords k WHERE k.article.articleCode IN (" +
            "  SELECT DISTINCT a.articleCode " +
            "  FROM Article a JOIN a.keywords k " +
            "  WHERE k.keyword LIKE :region " +
            "  AND FUNCTION('DATE', a.createdDate) = CURRENT_DATE)")
    List<KeyWords> findKeyWordsByRegion(@Param("region") String region);


}
