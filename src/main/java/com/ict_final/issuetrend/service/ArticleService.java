package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.request.ArtComRequestDTO;
import com.ict_final.issuetrend.dto.request.ArticleFilterRequestDTO;
import com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.ArticleComments;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.ArticleRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    LocalDate date = LocalDate.now().minusDays(1);

    public List<Article> getTodayArticles() {
//        return articleRepository.findArticleByDate(date);
        return articleRepository.findArticleByDate();
    }

    public List<Article> getTodayArticleByRegion(String region) {
        System.out.println("지역기사 가져오기 서비스");
        return articleRepository.findArticleByRegion(region);
//        return articleRepository.findArticleByRegion(region, date);
    }

    public List<Article> searchArticles(String keyword) {
       return articleRepository.findArticlesByKeyword(keyword);
    }

    public Article getArticleByCode(String articleCode) {
        return articleRepository.findByArticleCode(articleCode);
    }


    public List<Article> filterArticles(ArticleFilterRequestDTO filterRequestDTO) {
        return articleRepository.findArticlesByFilters(
                filterRequestDTO.getRegion(),
                filterRequestDTO.getNewsAgency(),
                filterRequestDTO.getSortOption(),
                filterRequestDTO.getKeyword()
        );
    }
}
