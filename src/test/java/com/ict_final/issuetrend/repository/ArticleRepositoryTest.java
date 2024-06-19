package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.KeyWords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository repository;

    @Test
    public void findArticleByDate() {
//        List<Article> articleByDate = repository.findArticleByDate(LocalDate.now().minusDays(1));
        List<Article> articleByDate = repository.findArticleByDate();

        for (Article article : articleByDate) {
            System.out.println("기사 코드 = " + article.getArticleCode());
        }
    }

    @Test
    public void findArticleByRegion() {
//        List<Article> articleByRegion = repository.findArticleByRegion("서울%", LocalDate.now().minusDays(1));
        List<Article> articleByRegion = repository.findArticleByRegion("서울%");

        for (Article article : articleByRegion) {
            System.out.println("기사 제목 = " + article.getTitle());
        }
    }

}