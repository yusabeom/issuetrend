package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class BoardPostController {

    private final BoardPostService boardPostService;

    // 게시글 전제 조회하기
    @GetMapping("/search-post")
    public ResponseEntity<?> searchPost() {

        try {
            List<PostResponseDTO> allPosts = boardPostService.findAllPosts();
            return ResponseEntity.ok().body(allPosts);
        } catch (Exception e) {
            log.error("게시글 전체 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // 게시글 등록

    // 게시글 삭제하기

}
