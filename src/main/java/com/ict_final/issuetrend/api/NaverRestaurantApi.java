package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.response.NaverRestResponseDto;
import com.ict_final.issuetrend.service.NaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// 네이버 맛집 API
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class NaverRestaurantApi {


    private final NaverService naverService;

    // 지역별 네이버 맛집 검색
    @GetMapping("/{regionName}")
    private ResponseEntity<?> getRestaurant(@PathVariable("regionName") String regionName) {
        log.info("/restaurant/{} - GET!", regionName);

        String query = regionName;

        try {
            List<NaverRestResponseDto> responseData = naverService.searchRestaurant(query);

            log.info("================= 응답 객체 =================== \n" + responseData.get(0).getRoadAddress());
            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity
                    .internalServerError() // 500번 응답코드
                    .body(e.getMessage());
        }


    }
// 지역별 이미지 검색
//    @GetMapping("/{search}")
//    private ResponseEntity<?> getRestaurant(@PathVariable("search") String search) {
//
//    }
//
}
