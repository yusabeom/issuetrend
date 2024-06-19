package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.PostRequestDTO;
import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.UserRepository;
import com.ict_final.issuetrend.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class BoardPostController {

    private final UserRepository userRepository;
    private final BoardPostService boardPostService;

    // 게시글 상세 조회
    @GetMapping("/search-post/{postNo}")
    public ResponseEntity<?> getPost(@PathVariable("postNo") Long postNo) {
        try {
            PostResponseDTO responseDTO = boardPostService.getPost(postNo);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


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
    // 게시글 등록하기
    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@RequestBody PostRequestDTO requestDTO) {
        try {
            BoardPost post = boardPostService.createPost(requestDTO);

            PostResponseDTO responseDTO = new PostResponseDTO(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            log.error("게시물 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 게시글 삭제하기
    @DeleteMapping("/delete-post/{postNo}")
    public ResponseEntity<?> deletePost(@PathVariable("postNo") Long postNo) {
        try {
            boardPostService.deletePost(postNo);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
