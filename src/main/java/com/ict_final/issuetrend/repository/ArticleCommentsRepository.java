package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.ArticleComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentsRepository extends JpaRepository<ArticleComments, Long> {
    List<ArticleComments> findByArticleArticleCode(String articleCode);
}
