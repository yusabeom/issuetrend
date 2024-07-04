package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.service.WeatherService;
import com.ict_final.issuetrend.dto.response.ForecastItemResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Weather API", description = "지역별 날씨 정보 api 입니다.")
@RestController
@RequestMapping("/issue-trend")
@Slf4j
@RequiredArgsConstructor
public class WeatherController {


    private final WeatherService weatherService;

    @Operation(summary = "날씨 데이터 조회", description = "지정된 좌표(nx, ny)에 대한 날씨 데이터 조회를 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "nx", description = "X 좌표(nx)", example = "60", required = true),
            @Parameter(name = "ny", description = "y 좌표(ny)", example = "120", required = true)
    })
    @GetMapping("/weather")
    public ResponseEntity<?> getWeatherData(@RequestParam String nx, @RequestParam String ny) {
        log.info("Received weather request for nx: {}, ny: {}", nx, ny);  // 요청 받은 좌표 로그
        try {
            Map<String, Map<String, String>> groupedData = weatherService.getGroupedForecastByTime(nx, ny);
            return ResponseEntity.ok(groupedData);
        } catch (IOException e) {
            log.error("Error retrieving weather data for nx: {}, ny: {}", nx, ny, e);  // 오류 로그
            return ResponseEntity.internalServerError().body("Error retrieving weather data");
        }
    }
}
