package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.request.PostRequestDTO;
import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardPostService {
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Value("${img.path}")
    private String imgFilePath;

    public List<PostResponseDTO> findAllPosts() {
        List<BoardPost> boardPostList = boardPostRepository.findAll();
        return boardPostList.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardPost createPost(PostRequestDTO requestDTO ,String imgFilePath) {

        User user = userRepository.findByUserNo(requestDTO.getUserNo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BoardPost boardPost = BoardPost.builder()
                .user(user)
                .title(requestDTO.getTitle())
                .text(requestDTO.getText())
                .img(imgFilePath)
                .build();
        log.info(boardPost.getUser().getEmail());
       return boardPostRepository.save(boardPost);
    }

    public BoardPost updatePost(Long postNo, PostRequestDTO requestDTO, MultipartFile newImage) throws IOException {
        // 게시글 조회
        BoardPost boardPost = boardPostRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 새 이미지 저장
        String newImagePath = boardPost.getImg(); // 기본적으로 기존 이미지를 유지
        if (newImage != null && !newImage.isEmpty()) {
            // 기존 이미지 삭제
            if (newImagePath != null) {
                String oldImagePath = imgFilePath + "/" + newImagePath;
                File oldImageFile = new File(oldImagePath);
                if (oldImageFile.exists()) {
                    oldImageFile.delete();
                }
            }
            // 새 이미지 업로드
            newImagePath = uploadProfileImage(newImage);
        }

        // 텍스트와 이미지 경로 업데이트
        boardPost.setTitle(requestDTO.getTitle());
        boardPost.setText(requestDTO.getText());
        boardPost.setImg(newImagePath);
        boardPost.setWriteDate(LocalDateTime.now());

        return boardPostRepository.save(boardPost);
    }

    public void deletePost(Long postNo) {
        boardPostRepository.deleteById(postNo);
    }

    public PostResponseDTO getPost(Long postNo) {
        BoardPost post = boardPostRepository.findById(postNo).orElseThrow(() -> new RuntimeException("Post not found"));
        return new PostResponseDTO(post);
    }

    public String uploadProfileImage(MultipartFile img) throws IOException {
        // 파일명을 유니크하게 변경 (이름 충돌 가능성을 대비)
        // UUID와 원본파일명을 결합
        String uniqueFileName = UUID.randomUUID() + "_" + img.getOriginalFilename();

        return s3Service.uploadToS3Bucket(img.getBytes(), uniqueFileName);
    }

    // 페이징 처리
    public List<PostResponseDTO> findPgaePosts(Long pageNo) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo.intValue() - 1, pageSize);
        Page<BoardPost> boardPostPage = boardPostRepository.findPagingPosts(pageable);
        return boardPostPage.getContent().stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }


    public List<PostResponseDTO> findUserPosts(Long userNo) {
        List<BoardPost> boardPosts = boardPostRepository.findbyUserNo(userNo);
        return boardPosts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }
}
