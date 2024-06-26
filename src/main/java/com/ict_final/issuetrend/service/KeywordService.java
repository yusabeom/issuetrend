package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.response.KeywordsFrequencyResponseDTO;
import com.ict_final.issuetrend.entity.KeyWords;
import com.ict_final.issuetrend.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;
    LocalDate date = LocalDate.now().minusDays(1);

    public List<KeywordsFrequencyResponseDTO> getTodayKeywordFrequency() {
        List<KeyWords> keyWordsList = keywordRepository.findKeyWordsByDate(date);
        List<String> keywords = new ArrayList<>();

        for (KeyWords kw : keyWordsList) {
            keywords.add(kw.getKeyword());
        }

        List<KeywordsFrequencyResponseDTO> frequencyList = calculateKeywordFrequency(keywords);

        return frequencyList;
    }

    public List<KeywordsFrequencyResponseDTO> getTodayKeywordByRegionFrequency(String region) {
        List<KeyWords> keyWordsList = keywordRepository.findKeyWordsByRegion(region, date);
        List<String> keywords = new ArrayList<>();

        for (KeyWords kw : keyWordsList) {
            keywords.add(kw.getKeyword());
        }

        List<KeywordsFrequencyResponseDTO> frequencyList = calculateKeywordFrequency(keywords);

        // 결과 출력
        for (KeywordsFrequencyResponseDTO kf : frequencyList) {
            System.out.println("키워드: " + kf.getKeyword() + ", 빈도수: " + kf.getFrequency());
        }

        return frequencyList;
    }

    public static List<KeywordsFrequencyResponseDTO> calculateKeywordFrequency(List<String> keywords) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String keyword : keywords) {
            frequencyMap.put(keyword, frequencyMap.getOrDefault(keyword, 0) + 1);
        }

        List<KeywordsFrequencyResponseDTO> frequencyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            frequencyList.add(new KeywordsFrequencyResponseDTO(entry.getKey(), entry.getValue()));
        }

        return frequencyList;
    }
}
