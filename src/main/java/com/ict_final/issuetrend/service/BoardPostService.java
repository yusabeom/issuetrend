package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.request.PostRequestDTO;
import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardPostService {
    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;

    public List<PostResponseDTO> findAllPosts() {
        List<BoardPost> boardPostList = boardPostRepository.findAll();
        return boardPostList.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoardPost createPost(PostRequestDTO requestDTO) {

        User user = userRepository.findByUserNo(requestDTO.getUserNo())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BoardPost boardPost = BoardPost.builder()
                .user(user)
                .text(requestDTO.getText())
                .img(requestDTO.getImg())
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
}
