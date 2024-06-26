package com.ict_final.issuetrend.service;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
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
public class DustService {

    public Map<String, String> getRegionDust(String region) {
        Map<String, String> dustInfo = new HashMap<>();

        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        String searchDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);
        log.info("기준 날짜: {}", searchDate);

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty");
            urlBuilder.append("?" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(region, "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8") );
            urlBuilder.append("&" +  URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=xslbsjNPICkf%2B2T2G6bme0BihSfnUHKHH4DmLkpTwudydktk7uUrVTvyu72ZUQQzU%2FwlUAA5tFoieiHXwVKZBA%3D%3D"); /*서비스명 (대소문자 구분 필수입니다.)*/
            urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8") );

            log.info("완성된 URL: {}", urlBuilder.toString());

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;

            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

            conn.disconnect();

//            log.info(sb.toString());

            String jsonString = sb.toString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONArray itemArray = (JSONArray) body.get("items");

            double totalPm10 = 0;
            int count = 0;

            for (Object obj : itemArray) {
                JSONObject item = (JSONObject) obj;
                String stationName = (String) item.get("sidoName");

                String pm10ValueStr = (String) item.get("pm10Value");
                if (pm10ValueStr != null && !pm10ValueStr.equals("-")) {
                    double pm10Value = Double.parseDouble(pm10ValueStr);
                    totalPm10 += pm10Value;
                    count++;
                }
            }

            if (count == 0) {
                dustInfo.put("region", region);
                dustInfo.put("average", "데이터 없음");
                dustInfo.put("grade", "데이터 없음");
            } else {
                double averagePm10 = totalPm10 / count;
                String formattedAverage = String.format("%.2f", averagePm10);

                log.info("지역 미세먼지 농도 평균: {}", formattedAverage);

                String grade;
                if (averagePm10 <= 30) {
                    grade = "좋음";
                } else if (averagePm10 <= 80) {
                    grade = "보통";
                } else if (averagePm10 <= 150) {
                    grade = "나쁨";
                } else {
                    grade = "매우 나쁨";
                }

                dustInfo.put("region", region);
                dustInfo.put("average", formattedAverage);
                dustInfo.put("grade", grade);
            }

        } catch (Exception e) {
            e.printStackTrace();
            dustInfo.put("region", region);
            dustInfo.put("average", "오류발생");
            dustInfo.put("grade", "오류발생");
        }

        return dustInfo;
    }
}
