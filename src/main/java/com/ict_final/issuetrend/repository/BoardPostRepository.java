package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
}
