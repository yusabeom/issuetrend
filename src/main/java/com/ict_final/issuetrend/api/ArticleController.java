package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.RegionRequestDTO;
import com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO;
import com.ict_final.issuetrend.dto.response.KeywordsFrequencyResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.service.ArticleService;
import com.ict_final.issuetrend.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class ArticleController {

    private final ArticleService articleService;
    private final KeywordService keywordService;

    // 오늘 기사 가져오기
    @GetMapping("/todayArticles")
    public ResponseEntity<?> todayArticles() {
        log.info("todayArticles GetMapping request!");

        List<Article> todayArticles = articleService.getTodayArticles();

        try {
            log.info("Article 개수 : " + todayArticles.size());
            List<ArticleDetailResponseDTO> responseDTOList = todayArticles.stream()
                    .map(ArticleDetailResponseDTO::new)
                    .toList();

            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            log.error("Error retrieving today's articles", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving today's articles");
        }
    }

    // 오늘 기사의 키워드별 개수
    @GetMapping("/todayKeywordsFrequency")
    public ResponseEntity<?> todayKeywordsFrequency() {
        log.info("todayKeywords GetMapping request!");

        try {
            List<KeywordsFrequencyResponseDTO> todayKeywordCounts = keywordService.getTodayKeywordFrequency();

            return ResponseEntity.ok().body(todayKeywordCounts);
        } catch (Exception e) {
            log.error("Error retrieving today's keywords", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving today's keywords");
        }
    }


    // 지역별 기사 가져오기
    @PostMapping("/todayArticles")
    public ResponseEntity<?> todayArticleByReigion(@RequestBody RegionRequestDTO requestDTO){
        log.info("todayArticles GetMapping request! region : {}", requestDTO.getRegion());

        List<Article> todayArticleByRegion = articleService.getTodayArticleByRegion(requestDTO.getRegion()+"%");

        try {
            log.info("Article 개수 : " + todayArticleByRegion.size());
            List<ArticleDetailResponseDTO> responseDTOList = todayArticleByRegion.stream()
                    .map(ArticleDetailResponseDTO::new)
                    .toList();

            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            log.error("Error retrieving today's articles", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving today's articles");
        }
    }

    // 지역별 기사의 키워드별 개수
    @PostMapping("/todayKeywordsFrequency")
    public ResponseEntity<?> todayKeywordsByRegionFrequency(@RequestBody RegionRequestDTO requestDTO) {
        log.info("todayKeywords GetMapping request! region : {}", requestDTO.getRegion());

        try {
            List<KeywordsFrequencyResponseDTO> todayKeywordCounts = keywordService.getTodayKeywordByRegionFrequency(requestDTO.getRegion());

            return ResponseEntity.ok().body(todayKeywordCounts);
        } catch (Exception e) {
            log.error("Error retrieving today's keywords", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving today's keywords");
        }
    }
}
