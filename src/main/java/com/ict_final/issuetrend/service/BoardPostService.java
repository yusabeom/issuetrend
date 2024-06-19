package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.response.PostResponseDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.repository.BoardPostRepository;
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

    public List<PostResponseDTO> findAllPosts() {
        List<BoardPost> boardPostList = boardPostRepository.findAll();
        return boardPostList.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
    }
}
