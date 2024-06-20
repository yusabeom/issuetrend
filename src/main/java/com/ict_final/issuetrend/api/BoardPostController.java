package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.PostRequestDTO;
import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import com.ict_final.issuetrend.service.BoardPostService;
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
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class BoardPostController {

    private final BoardPostRepository boardPostRepository;
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
    @GetMapping("/load-image/{postNo}")
    public ResponseEntity<?> loadImage(@PathVariable Long postNo) {
        BoardPost post = boardPostRepository.findById(postNo).orElseThrow();
        try {
            // 1. 프로필 사진의 경로부터 얻어야 한다.
            String filePath = boardPostService.findProfilePath(post.getImg());
            log.info("filePath: {}", filePath);

            // 2. 얻어낸 파일 경로를 통해 실제 파일 데이터를 로드하기.
            File profileFile = new File(filePath);

            // 만약 존재하지 않는 경로라면 클라이언트로 404 status를 리턴.
            if (!profileFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 해당 경로에 저장된 파일을 바이트 배열로 직렬화 해서 리턴
            byte[] fileData = FileCopyUtils.copyToByteArray(profileFile);

            // 3. 응답 헤더에 컨텐츠 타입을 설정
            HttpHeaders headers = new HttpHeaders();
            MediaType contentType = UserController.findExtensionAndGetMediaType(filePath);
            if (contentType == null) {
                return ResponseEntity.internalServerError()
                        .body("발견된 파일은 이미지 파일이 아닙니다.");
            }
            headers.setContentType(contentType);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 게시글 수정하기
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
