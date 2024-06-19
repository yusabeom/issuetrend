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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
                .text(requestDTO.getText())
                .img(imgFilePath)
                .build();
        log.info(boardPost.getUser().getEmail());
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
        // 루트 디렉토리가 실존하는 지 확인 후 존재하지 않으면 생성.
        File root = new File(imgFilePath);
        if (!root.exists()) root.mkdirs();
        // 파일명을 유니크하게 변경 (이름 충돌 가능성을 대비)
        // UUID와 원본파일명을 결합
        String uniqueFileName = UUID.randomUUID() + "_" + img.getOriginalFilename();
        // 파일 저장
        File uploadFile = new File(root, uniqueFileName); // 경로 설정을 수정함
        img.transferTo(uploadFile);
        return uniqueFileName;
    }

    public String findProfilePath(String img) {
        // DB에는 파일명만 저장. -> service가 가지고 있는 Root Path와 연결해서 리턴
        return imgFilePath + "/" + img;
    }
}
