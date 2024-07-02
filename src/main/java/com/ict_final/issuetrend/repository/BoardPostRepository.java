package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
    @Query("SELECT b FROM BoardPost b ORDER BY b.postNo DESC")
    Page<BoardPost> findPagingPosts(Pageable pageable);

    @Query("SELECT b FROM BoardPost b WHERE b.user.userNo = :userNo")
    List<BoardPost> findbyUserNo(Long userNo);

}
