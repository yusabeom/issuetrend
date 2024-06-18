package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    LocalDate date = LocalDate.now().minusDays(1);

    public List<Article> getTodayArticles() {
        return articleRepository.findArticleByDate(date);
    }

    public List<Article> getTodayArticleByRegion(String region) {
        return articleRepository.findArticleByRegion(region, date);
    }

    public List<Article> searchArticles(String keyword) {
       return articleRepository.findAtriclesByKeyword(keyword);
    }

    public Article getArticleByCode(String articleCode) {
        return articleRepository.findByArticleCode(articleCode);
    }
}
