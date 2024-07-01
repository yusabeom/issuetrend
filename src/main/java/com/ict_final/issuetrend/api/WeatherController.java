package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.service.WeatherService;
import com.ict_final.issuetrend.dto.response.ForecastItemResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/issue-trend")
@Slf4j
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

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
