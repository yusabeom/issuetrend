package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.ArtComRequestDTO;
import com.ict_final.issuetrend.dto.request.RegionRequestDTO;
import com.ict_final.issuetrend.dto.response.ArtComResponseDTO;
import com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO;
import com.ict_final.issuetrend.dto.response.KeywordsFrequencyResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.SearchTerm;
import com.ict_final.issuetrend.entity.ArticleComments;
import com.ict_final.issuetrend.service.ArticleCommentsService;
import com.ict_final.issuetrend.service.ArticleService;
import com.ict_final.issuetrend.service.KeywordService;
import com.ict_final.issuetrend.service.SearchTermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class ArticleController {

    private final ArticleService articleService;
    private final KeywordService keywordService;
    private final ArticleCommentsService articleCommentsService;
    private final SearchTermService searchTermService;

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

    // 키워드로 기사 검색하기
    @GetMapping("/search")
    public ResponseEntity<?> searchArticles(@RequestParam("keyword") String keyword) {
        log.info("Searching articles for keyword: {}", keyword);

        // 검색한 키워드 저장
        searchTermService.saveSearchTerm(new SearchTerm(keyword));

        try {
            // 공백 입력시 badRequest 도출
            if (keyword == null || keyword.trim().isEmpty()) {
                log.info("Empty keyword provided.");
                return ResponseEntity.badRequest().body("Empty keyword provided.");
            }

            List<Article> searchedArticles = articleService.searchArticles(keyword);

            if (searchedArticles.isEmpty()) {
                log.info("No articles: {}", keyword);
                return ResponseEntity.noContent().build();
            }

            List<ArticleDetailResponseDTO> responseDTOList = searchedArticles.stream()
                    .map(ArticleDetailResponseDTO::new)
                    .toList();

            return ResponseEntity.ok().body(responseDTOList);
        } catch (Exception e) {
            log.error("Error searching articles", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching articles");
        }
    }

    // 기사별 상세 페이지 (특정 기사 조회)
    @GetMapping("/articles/{articleCode}")
    public ResponseEntity<?> getArticleDetail(@PathVariable("articleCode") String articleCode) {
        log.info("Fetching article with code: {}", articleCode);

        try {
            Article article = articleService.getArticleByCode(articleCode);

            if (article == null) {
                log.info("No article found with code: {}", articleCode);
                return ResponseEntity.noContent().build();
            }

            ArticleDetailResponseDTO responseDTO = new ArticleDetailResponseDTO(article);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.error("Error fetching article", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching article");
        }
    }

    // 기사별 댓글 작성
    @PostMapping("/articles/{articleCode}/comments")
    private ResponseEntity<?> createCommentByArticle(@RequestBody ArtComRequestDTO requestDTO) {
        try {
            ArticleComments comment = articleCommentsService.createComment(requestDTO);
            return ResponseEntity.ok().body(new  ArtComResponseDTO(comment));
        } catch (Exception e) {
            log.error("Error adding comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding comment: " + e.getMessage());
        }
    }

    // 기사별 댓글 전체조회
    @GetMapping("/articles/{articleCode}/comments")
    private ResponseEntity<?> getCommentByArticle(@PathVariable("articleCode") String articleCode) {
        try {
            List<ArticleComments> comments = articleCommentsService.getCommentsByArticleCode(articleCode);
            List<ArtComResponseDTO> collect = comments.stream()
                    .map(ArtComResponseDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(collect);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching comments");
        }
    }

    // 기사별 댓글 수정
    @PutMapping("/articles/{articleCode}/comments/{commentNo}")
    public ResponseEntity<?> updateCommentByArticle(
            @PathVariable("articleCode") String articleCode,
            @PathVariable("commentNo") Long commentNo,
            @RequestBody Map<String, String> request) {
        log.info("Updating comment with num {} for article {}", commentNo, articleCode);

        try {
            // 댓글 조회
            Optional<ArticleComments> optionalComment = articleCommentsService.findByCommentNo(commentNo);
            if (optionalComment.isEmpty()) {
                log.info("Comment with num {} not found", commentNo);
                return ResponseEntity.notFound().build();
            }

            ArticleComments comment = optionalComment.get();

            // 요청 바디에서 수정할 내용 가져오기
            String updatedText = request.get("text");
            if (updatedText == null || updatedText.trim().isEmpty()) {
                log.info("Empty or missing 'text' field in request body");
                return ResponseEntity.badRequest().body("Empty or missing 'text' field in request body");
            }

            // 댓글 내용 업데이트
            comment.setText(updatedText);

            // 변경 사항을 데이터베이스에 저장
            articleCommentsService.updateComment(comment);

            // 수정된 댓글 응답
            return ResponseEntity.ok().body(new ArtComResponseDTO(comment));
        } catch (Exception e) {
            log.error("Error updating comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating comment");
        }
    }

    // 기사별 댓글 삭제
    @DeleteMapping("/articles/{articleCode}/comments/{commentNo}")
    public ResponseEntity<?> deleteCommentByArticle(
            @PathVariable("articleCode") String articleCode,
            @PathVariable("commentNo") Long commentNo) {
        log.info("Deleting comment with id {} for article {}", commentNo, articleCode);

        try {
            // 댓글 조회
            Optional<ArticleComments> optionalComment = articleCommentsService.findByCommentNo(commentNo);
            if (optionalComment.isEmpty()) {
                log.info("Comment with id {} not found", commentNo);
                return ResponseEntity.notFound().build();
            }

            ArticleComments comment = optionalComment.get();

            // 댓글 삭제
            articleCommentsService.deleteComment(comment);

            // 삭제 성공 응답
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting comment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting comment");
        }
    }

}
