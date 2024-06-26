package com.ict_final.issuetrend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.serviceKey}")
    private String serviceKey;

    public Map<String, String> getShortTermForecast() throws IOException {
        Map<String, String> weatherData = new HashMap<>(); // 여기에서 weatherData를 초기화합니다.
        LocalDateTime now = LocalDateTime.now();
        String baseDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
        LocalDateTime timeWithFixedMinutes = now.withMinute(30);  // 시간을 HH:30으로 설정
        String baseTime = DateTimeFormatter.ofPattern("HHmm").format(timeWithFixedMinutes);
        try {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=100");
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            log.info("Response code: {}", conn.getResponseCode());

            BufferedReader rd = conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
                    ? new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    : new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(sb.toString());
            JSONObject response = (JSONObject) jsonResponse.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONArray items = (JSONArray) ((JSONObject) body.get("items")).get("item");

            for (Object item : items) {
                JSONObject jsonItem = (JSONObject) item;
                String category = (String) jsonItem.get("category");
                String fcstValue = (String) jsonItem.get("fcstValue");
                weatherData.put(category, fcstValue);
            }
        } catch (Exception e) {
            log.error("Error fetching or parsing weather data: {}", e.getMessage());
            throw new IOException("Failed to fetch or parse weather data", e);
        }

        return weatherData;
    }
}