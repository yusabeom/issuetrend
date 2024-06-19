package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.SearchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SearchTermRepository extends JpaRepository<SearchTerm, Long> {

    @Query("SELECT s FROM SearchTerm s GROUP BY s.searchTerm ORDER BY COUNT(s.searchTerm) DESC")
    List<SearchTerm> popular();

}
