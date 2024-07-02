package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.auth.TokenUserInfo;
import com.ict_final.issuetrend.dto.request.ScrapRequestDTO;
import com.ict_final.issuetrend.dto.response.ArticleDetailResponseDTO;
import com.ict_final.issuetrend.dto.response.ScrapResponseDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.service.S3Service;
import com.ict_final.issuetrend.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Scrap API", description = "회원이 스크랩한 기사를 저장, 조회, 삭제하는 api 입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend/scrap")
public class ScrapController {

    private final ScrapService scrapService;
    private final S3Service s3Service;

    @Operation(summary = "스크랩 등록", description = "기사 스크랩을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요.", example = "1", required = true),
            @Parameter(name = "articleCode", description = "기사 고유 번호를 작성하세요.", example = "1", required = true)
    })
    @PostMapping
    public ResponseEntity<?> scrapArticle(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @RequestBody ScrapRequestDTO requestDTO) {
        scrapService.scrapArticle(tokenUserInfo, requestDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스크랩 조회", description = "회원의 스크랩을 조회하는 메서드 입니다.")
    @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요.", example = "1", required = true)
    @GetMapping("/user")
    public ResponseEntity<List<ArticleDetailResponseDTO>> getScrappedArticles(@AuthenticationPrincipal TokenUserInfo tokenUserInfo) {
        List<Article> articles = scrapService.getScrappedArticles(tokenUserInfo.getUserNo());
        List<ArticleDetailResponseDTO> articleDetails = articles.stream()
                .map(ArticleDetailResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(articleDetails);
    }

    @Operation(summary = "스크랩 삭제", description = "회원이 등록한 스크랩을 삭제하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요.", example = "1", required = true),
            @Parameter(name = "articleCode", description = "기사 고유 번호를 작성하세요.", example = "1", required = true)

    })
    @DeleteMapping("/delete/{articleCode}")
    public ResponseEntity<Void> deleteScrap(@AuthenticationPrincipal TokenUserInfo tokenUserInfo, @PathVariable("articleCode") String articleCode) {
        scrapService.deleteScrap(tokenUserInfo.getUserNo(), articleCode);
        return ResponseEntity.ok().build();
    }

}
