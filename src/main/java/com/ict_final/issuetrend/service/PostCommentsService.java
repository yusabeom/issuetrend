package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.request.BoardComRequestDTO;
import com.ict_final.issuetrend.entity.BoardPost;
import com.ict_final.issuetrend.entity.PostComments;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.BoardPostRepository;
import com.ict_final.issuetrend.repository.PostCommentRepository;
import com.ict_final.issuetrend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class PostCommentsService {

    private final BoardPostRepository boardPostRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;

    public PostComments createPostComment(BoardComRequestDTO requestDTO) {
        User user = userRepository.findByUserNo(requestDTO.getUserNo()).orElseThrow();
        BoardPost post = boardPostRepository.findById(requestDTO.getPostNo()).orElseThrow();

        PostComments comments = PostComments.builder()
                .user(user)
                .boardPost(post)
                .text(requestDTO.getText())
                .build();
        return postCommentRepository.save(comments);
    }

    public List<PostComments> getPostCommentsByPostNo(Long postNo) {
        return postCommentRepository.findByBoardPostPostNo(postNo);
    }

    // 댓글 수정
    public PostComments updatePostComment(BoardComRequestDTO requestDTO) {
        Optional<PostComments> optionalComment = postCommentRepository.findById(requestDTO.getPostNo());
        if (optionalComment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id " + requestDTO.getPostNo() + " not found");
        }

        PostComments comment = optionalComment.get();
        comment.setText(requestDTO.getText());  // 텍스트 업데이트
        // 여기에 다른 필드도 필요에 따라 업데이트 할 수 있음

        return postCommentRepository.save(comment);
    }

    // 댓글 삭제
    public void deletePostComment(Long commentNo) {
        Optional<PostComments> optionalComment = postCommentRepository.findById(commentNo);
        if (optionalComment.isEmpty()) {
            throw new EntityNotFoundException("Comment with id " + commentNo + " not found");
        }

        PostComments comment = optionalComment.get();
        postCommentRepository.delete(comment);
    }
}
