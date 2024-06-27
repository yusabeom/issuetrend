package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.entity.SearchTerm;
import com.ict_final.issuetrend.repository.SearchTermRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SearchTermService {

    private final SearchTermRepository searchTermRepository;

    public void saveSearchTerm(SearchTerm searchTerm) {
        searchTermRepository.save(searchTerm);
    }

    public List<String> popularSearchTerm() {
        List<SearchTerm> searchTermList = searchTermRepository.popular();
        System.out.println("searchTermList = " + searchTermList);

        List<String> popularList = searchTermList.stream()
                .map(SearchTerm::getSearchTerm)
                .collect(Collectors.toList()).subList(0,5);

        System.out.println("popularList = " + popularList);
        return popularList;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 0 * * ?")    // 매일 자정 리셋
    @Transactional
    public void deleteAllSearchTerms() {
        int deletedCount = entityManager.createQuery("DELETE FROM SearchTerm").executeUpdate();
        System.out.println(deletedCount + "개의 항목이 삭제되었습니다.");
    }
}
