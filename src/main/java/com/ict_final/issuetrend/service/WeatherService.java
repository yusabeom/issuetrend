package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.response.ForecastItemResponseDTO;
import lombok.Getter;
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
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.serviceKey}")
    private String serviceKey;

    public List<ForecastItemResponseDTO> getShortTermForecast(String nx, String ny) throws IOException {
        List<ForecastItemResponseDTO> forecastItems = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String baseDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(now);
        String baseTime = DateTimeFormatter.ofPattern("HH'00'").format(now.minusHours(1));

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=100");
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=JSON");
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            log.info("Constructed URL: {}", url.toString());
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
                forecastItems.add(new ForecastItemResponseDTO(
                        (String) jsonItem.get("category"),
                        (String) jsonItem.get("fcstTime"),
                        (String) jsonItem.get("fcstValue")
                ));
            }
        } catch (Exception e) {
            log.error("Error fetching or parsing weather data: {}", e.getMessage());
            throw new IOException("Failed to fetch or parse weather data", e);
        }

        return forecastItems;
    }

    public Map<String, Map<String, String>> getGroupedForecastByTime(String nx, String ny) throws IOException {
        List<ForecastItemResponseDTO> forecastItems = getShortTermForecast(nx, ny);
        if (forecastItems.isEmpty()) {
            log.warn("No data returned for nx: {}, ny: {}", nx, ny);
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> groupedData = forecastItems.stream()
                .collect(Collectors.groupingBy(
                        ForecastItemResponseDTO::getFcstTime,
                        TreeMap::new,
                        Collectors.toMap(
                                ForecastItemResponseDTO::getCategory,
                                ForecastItemResponseDTO::getFcstValue,
                                (oldValue, newValue) -> oldValue,
                                LinkedHashMap::new
                        )
                ));
        log.info("Grouped data: {}", groupedData);
        return groupedData;
    }
}
