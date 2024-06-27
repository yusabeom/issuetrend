package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.BoardComRequestDTO;
import com.ict_final.issuetrend.dto.request.PostRequestDTO;
import com.ict_final.issuetrend.dto.response.BoardComResponseDTO;
import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.PostComments;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import com.ict_final.issuetrend.service.BoardPostService;
import com.ict_final.issuetrend.service.PostCommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "BoardPost API", description = "게시물과 게시물별 댓글 작성 및 수정, 삭제 api 입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class BoardPostController {

    private final PostCommentsService postCommentsService;
    private final BoardPostRepository boardPostRepository;
    private final BoardPostService boardPostService;

    // 게시글 상세 조회
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회를 담당하는 메서드 입니다.")
    @Parameter(name = "postNo", description = "해당 게시물의 번호를 작성하세요.", example = "1", required = true)
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
    @Operation(summary = "게시물 전체 조회", description = "게시물 전체 조회를 담당하는 메서드 입니다.")
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
    @Operation(summary = "게시물 등록", description = "게시물 등록을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "title",description = "게시물 제목을 작성하세요", example = "제목입니다.", required = true),
            @Parameter(name = "text",description = "게시물 내용을 작성하세요", example = "내용입니다.", required = true),
            @Parameter(name = "img",description = "프로필 사진 경로를 작성하세요")
    })
    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(
            @RequestPart("requestDTO") PostRequestDTO requestDTO,
            @RequestPart(value = "img", required = false) MultipartFile img
    ) {
        try {
            String imgFilePath = null;
            if (img != null) {
                log.info("attached file name: {}", img.getOriginalFilename());
                // 전달받은 프로필 이미지를 먼저 지정된 경로에 저장한 후 저장 경로를 DB에 세팅하자.
                imgFilePath = boardPostService.uploadProfileImage(img);
            }

            BoardPost post = boardPostService.createPost(requestDTO, imgFilePath);
            PostResponseDTO responseDTO = new PostResponseDTO(post);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            log.error("게시물 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 게시글 이미지 가져오기
    @Operation(summary = "게시물 이미지 가져오기", description = "게시물 이미지로드를 담당하는 메서드 입니다.")
    @Parameter(name = "postNo", description = "수정할 게시물 고유 번호를 작성하세요", example = "1", required = true)
    @GetMapping("/load-image/{postNo}")
    public ResponseEntity<?> loadImage(@PathVariable Long postNo) {
        BoardPost post = boardPostRepository.findById(postNo).orElseThrow();

            // 1. 프로필 사진의 경로부터 얻어야 한다.
            String filePath = post.getImg();
            log.info("filePath: {}", filePath);

            if (filePath != null) { return ResponseEntity.ok().body(filePath); }
            else { return ResponseEntity.notFound().build(); }
    }

    // 게시글 수정하기
    @Operation(summary = "게시물 수정", description = "게시물 수정을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "postNo", description = "수정할 게시물 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "title",description = "게시물 제목을 작성하세요", example = "제목입니다.", required = true),
            @Parameter(name = "text",description = "게시물 내용을 작성하세요", example = "내용입니다.", required = true),
            @Parameter(name = "newImage",description = "프로필 사진 경로를 작성하세요")
    })
     @PutMapping("/update-post/{postNo}")
     public ResponseEntity<?> updatePost(
             @PathVariable Long postNo,
             @RequestPart("requestDTO") PostRequestDTO requestDTO,
             @RequestPart(name = "newImage", required = false) MultipartFile newImage) {

         try {
             BoardPost updatedPost = boardPostService.updatePost(postNo, requestDTO, newImage);
             return ResponseEntity.ok(new PostResponseDTO(updatedPost));
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating post");
         }
     }

    // 게시글 삭제하기
    @Operation(summary = "게시물 삭제", description = "게시물 삭제를 담당하는 메서드 입니다.")
    @Parameter(name = "postNo", description = "해당 게시물의 번호를 작성하세요.", example = "1", required = true)
    @DeleteMapping("/delete-post/{postNo}")
    public ResponseEntity<?> deletePost(@PathVariable("postNo") Long postNo) {
        try {
            boardPostService.deletePost(postNo);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 게시글 댓글 작성하기
    @Operation(summary = "게시물 댓글 작성", description = "게시물 댓글 작성을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "postNo", description = "작성할 게시물 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "text",description = "게시물 내용을 작성하세요", example = "내용입니다.", required = true)
    })
    @PostMapping("/post/{postNo}/comments")
    public ResponseEntity<?> createPostComment(@RequestBody BoardComRequestDTO requestDTO) {
        try {
            PostComments postComment = postCommentsService.createPostComment(requestDTO);
            return ResponseEntity.ok().body(new BoardComResponseDTO(postComment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding comment: " + e.getMessage());
        }
    }

    // 게시글 댓글 조회하기
    @Operation(summary = "게시물 댓글 조회", description = "게시물 댓글 조회를 담당하는 메서드 입니다.")
    @Parameter(name = "postNo", description = "해당 게시물의 번호를 작성하세요.", example = "1", required = true)
    @GetMapping("/post/{postNo}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable("postNo") Long postNo) {
        try {
            List<PostComments> comments = postCommentsService.getPostCommentsByPostNo(postNo);
            List<BoardComResponseDTO> collect = comments.stream()
                    .map(BoardComResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(collect);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching comments");
        }
    }

    // 게시글 댓글 수정하기
    @Operation(summary = "게시물 댓글 수정", description = "게시물 댓글 수정을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "postNo", description = "작성할 게시물 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "text",description = "게시물 내용을 작성하세요", example = "내용입니다.", required = true)
    })
    @PutMapping("/post/{postNo}/comments/{commentNo}")
    public ResponseEntity<?> updatePostComment(@RequestBody BoardComRequestDTO requestDTO) {
        try {
            PostComments updatedComment = postCommentsService.updatePostComment(requestDTO);
            return ResponseEntity.ok().body(new BoardComResponseDTO(updatedComment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating comment: " + e.getMessage());
        }
    }

    // 게시글 댓글 삭제하기
    @Operation(summary = "게시물 댓글 삭제", description = "게시물 댓글 삭제를 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "postNo", description = "해당 게시물 고유 번호를 작성하세요"),
            @Parameter(name = "commentNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
    })
    @DeleteMapping("/post/{postNo}/comments/{commentNo}")
    public ResponseEntity<?> deletePostComment(
            @PathVariable("postNo") Long postNo,
            @PathVariable("commentNo") Long commentNo) {
        try {
            postCommentsService.deletePostComment(commentNo);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting comment: " + e.getMessage());
        }
    }


}
