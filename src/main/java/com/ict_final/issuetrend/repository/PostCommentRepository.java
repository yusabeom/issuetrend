package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComments, Long> {
    List<PostComments> findByBoardPostPostNo(Long postNo);

    @Query("SELECT p FROM PostComments p WHERE p.user.userNo = :userNo")
    List<PostComments> findByBoardpostUserNo(Long userNo);
}
