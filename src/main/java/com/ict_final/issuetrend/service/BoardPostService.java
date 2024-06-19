package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardPostService {
    private final BoardPostRepository boardPostRepository;

    public List<BoardPost> findAllPosts() {
        boardPostRepository.findAll();
    }
}
