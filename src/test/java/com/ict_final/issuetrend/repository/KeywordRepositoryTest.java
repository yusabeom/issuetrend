package com.ict_final.issuetrend.repository;

import com.ict_final.issuetrend.entity.KeyWords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository repository;

    @Test
    public void findKeyWordsByDate() {
//        List<KeyWords> keyWordsByDate = repository.findKeyWordsByDate(LocalDate.now().minusDays(1));
        List<KeyWords> keyWordsByDate = repository.findKeyWordsByDate();

        for (KeyWords keyWords : keyWordsByDate) {
            System.out.println("keyWords.getKeyword() = " + keyWords.getKeyword());
        }
    }

    @Test
    public void findKeyWordsByRegion() {
//        List<KeyWords> keywordsByRegion = repository.findKeyWordsByRegion("서울%", LocalDate.now().minusDays(1));
        List<KeyWords> keywordsByRegion = repository.findKeyWordsByRegion("서울%");

        for (KeyWords keyWords : keywordsByRegion) {
            System.out.println("키워드 = " + keyWords.getKeyword());
        }
    }
}