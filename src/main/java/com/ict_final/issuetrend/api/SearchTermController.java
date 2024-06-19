package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.entity.SearchTerm;
import com.ict_final.issuetrend.service.SearchTermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class SearchTermController {

    private final SearchTermService searchTermService;

    // 실시간 인기 검색어 가져오기
    @GetMapping("/popular")
    public ResponseEntity<?> popularSearchTerm() {
        log.info("popularSearchTerm GetMapping request!");

        List<String> popularList = searchTermService.popularSearchTerm();


        try {
            return ResponseEntity.ok().body(popularList);
        } catch (Exception e) {
            log.error("Error retrieving popular search terms", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving popular search terms");
        }
    }

}
