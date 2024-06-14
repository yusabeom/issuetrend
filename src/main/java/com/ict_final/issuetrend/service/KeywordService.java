package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.entity.KeyWords;
import com.ict_final.issuetrend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Map<String, Long> getTodayKeywordFrequency() {
        List<KeyWords> keyWordsList = keywordRepository.findKeyWordsByDate();

        return keyWordsList.stream()
                .collect(Collectors.groupingBy(KeyWords::getKeyword, Collectors.counting()));
    }

    public Map<String, Long> getTodayKeywordByRegionFrequency(String region) {
        List<KeyWords> keyWordsList = keywordRepository.findKeyWordsByRegion(region);

        return keyWordsList.stream()
                .collect(Collectors.groupingBy(KeyWords::getKeyword, Collectors.counting()));
    }
}
