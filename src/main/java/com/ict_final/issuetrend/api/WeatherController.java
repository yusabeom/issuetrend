package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> getWeatherData() {
        try {
            Map<String, String> weatherData = weatherService.getShortTermForecast(); // 날씨 정보를 Map 형태로 반환
            return ResponseEntity.ok(weatherData);
        } catch (IOException e) {
            log.error("Error retrieving weather data", e);
            return ResponseEntity.internalServerError().body("Error retrieving weather data");
        }
    }
}
