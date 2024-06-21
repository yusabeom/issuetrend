package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.Scrap;
import com.ict_final.issuetrend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByUser(User user);

    @Query("SELECT s FROM Scrap s WHERE s.user.userNo = :userNo AND s.article.articleCode = :articleCode")
    Optional<Scrap> findByUserNoAndArticleCode(@Param("userNo") Long userNo, @Param("articleCode") String articleCode);
}
