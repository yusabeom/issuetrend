package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.FavoriteKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteKeywordRepository extends JpaRepository<FavoriteKeyword, Long> {

    List<FavoriteKeyword> findByUser_UserNo(Long userNo);
}
