package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.request.ArtComRequestDTO;
import com.ict_final.issuetrend.entity.Article;
import com.ict_final.issuetrend.entity.ArticleComments;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.ArticleCommentsRepository;
import com.ict_final.issuetrend.repository.ArticleRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleCommentsService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleCommentsRepository articleCommentsRepository;

    public ArticleComments createComment(ArtComRequestDTO requestDTO) {
        Article article = articleRepository.findByArticleCode(requestDTO.getArticleCode());
        User user = userRepository.findByUserNo(requestDTO.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userNo: " + requestDTO.getUserNo()));

        ArticleComments comment = ArticleComments.builder()
                .article(article)
                .user(user)
                .text(requestDTO.getText())
                .build();

        return articleCommentsRepository.save(comment);
    }

    public List<ArticleComments> getCommentsByArticleCode(String articleCode) {
        return articleCommentsRepository.findByArticleArticleCode(articleCode);
    }

    public Optional<ArticleComments> findByCommentNo(Long commentNo) {
        return articleCommentsRepository.findById(commentNo);
    }

    public ArticleComments updateComment(ArticleComments comment) {
        return articleCommentsRepository.save(comment);
    }

    public void deleteComment(ArticleComments comment) {
        articleCommentsRepository.delete(comment);
    }
}
