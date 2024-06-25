package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/issue-trend")
@Slf4j
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    // args를 경로 변수로 포함시킨 버전
    @GetMapping("/weather")
    public ResponseEntity<?> apiRequest() throws IOException {
        String json = String.valueOf(weatherService.getShortTermForecast()); // 날씨 정보를 JSON 문자열로 반환
        return ResponseEntity.ok(json);
    }
}