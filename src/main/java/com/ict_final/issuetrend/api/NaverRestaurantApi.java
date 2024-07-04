package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.response.NaverImageResponseDto;
import com.ict_final.issuetrend.dto.response.NaverRestResponseDto;
import com.ict_final.issuetrend.service.NaverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 네이버 맛집 API
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class NaverRestaurantApi {


    private final NaverService naverService;

    // 지역별 네이버 맛집 검색
    @Operation(summary = "네이버 지역 맛집 검색", description = "검색 키워드에 따라 네이버 지도의 가맹점 정보가 나오는 메서드입니다.")
    @Parameter(name = "regionName", description = "지역과 검색 키워드(메뉴)를 작성하세요.", example = "신촌 곱창", required = true)
    @GetMapping("/{regionName}")
    private ResponseEntity<?> getRestaurant(@PathVariable("regionName") String regionName) {
        log.info("/restaurant/{} - GET!", regionName);

        String query = regionName;

        try {
            List<NaverRestResponseDto> responseData = naverService.searchRestaurant(query);

//            log.info("================= 응답 객체 =================== \n" + responseData.get(0).getRoadAddress());
            return ResponseEntity.ok().body(responseData);
        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity
                    .internalServerError() // 500번 응답코드
                    .body(e.getMessage());
        }


    }
     // 이미지 검색
     @Operation(summary = "네이버 이미지 검색", description = "검색 키워드에 따라 네이버 검색 엔진을 통해 이미지를 출력하는 메서드입니다.")
     @Parameter(name = "search", description = "검색 키워드(메뉴)를 작성하세요.", example = "냉면", required = true)
    @GetMapping("/image/{search}")
    private ResponseEntity<?> getImage(@PathVariable("search") String search) {
        log.info("/restaurant/image/{} - GET!", search);

        try {
            List<NaverImageResponseDto> naverImageResponseDtos = naverService.searchDishImg(search);

//            log.info("================= 이미지 =================== \n" + naverImageResponseDtos.get(1).toString());
            return ResponseEntity.ok().body(naverImageResponseDtos);

        } catch (Exception e) {
            log.error("Error fetching comments", e);
            return ResponseEntity
                    .internalServerError() // 500번 응답코드
                    .body(e.getMessage());
        }

    }




}
