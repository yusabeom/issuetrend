package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.ScrapRequestDTO;
import com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO;
import com.ict_final.issuetrend.dto.response.ScrapResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.service.ScrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    public ResponseEntity<?> scrapArticle(@RequestBody ScrapRequestDTO requestDTO) {
        scrapService.scrapArticle(requestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userNo}")
    public ResponseEntity<List<ArticleDetailResponseDTO>> getScrappedArticles(@PathVariable("userNo") Long userNo) {
        List<Article> articles = scrapService.getScrappedArticles(userNo);
        List<ArticleDetailResponseDTO> articleDetails = articles.stream()
                .map(ArticleDetailResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(articleDetails);
    }

    @DeleteMapping("/{userNo}/{articleCode}")
    public ResponseEntity<Void> deleteScrap(@PathVariable("userNo") Long userNo, @PathVariable("articleCode") String articleCode) {
        scrapService.deleteScrap(userNo, articleCode);
        return ResponseEntity.ok().build();
    }

}
