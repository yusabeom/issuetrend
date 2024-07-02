package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.auth.TokenUserInfo;
import com.ict_final.issuetrend.dto.request.ScrapRequestDTO;
import com.ict_final.issuetrend.dto.response.ScrapResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.Scrap;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.ArticleRepository;
import com.ict_final.issuetrend.repository.ScrapRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ScrapService {

    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final ArticleRepository articleRepository;


    @Transactional
    public void scrapArticle(TokenUserInfo tokenUserInfo, ScrapRequestDTO requestDTO) {
        User user = userRepository.findByUserNo(tokenUserInfo.getUserNo())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Article article = articleRepository.findByArticleCode(requestDTO.getArticleCode());

        // 중복 스크랩 방지
        if (scrapRepository.findByUserNoAndArticleCode(user.getUserNo(), article.getArticleCode()).isPresent()) {
            throw new RuntimeException("This article is already scrapped by the user");
        }

        Scrap scrap = new Scrap();
        scrap.setUser(user);
        scrap.setArticle(article);

        scrapRepository.save(scrap);
    }

    public List<Article> getScrappedArticles(Long userNo) {
        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Scrap> scraps = scrapRepository.findByUser(user);
        return scraps.stream()
                .map(Scrap::getArticle)
                .collect(Collectors.toList());
    }

    public void deleteScrap(Long userNo, String articleCode) {

        Scrap scrap = scrapRepository.findByUserNoAndArticleCode(userNo, articleCode)
                .orElseThrow(() -> new RuntimeException("Scrap not found"));

        scrapRepository.delete(scrap);
    }
}
