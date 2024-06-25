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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class dustService {


    public String getRegionDust(String region) {

        Map<String, Object> resultMap = new HashMap<>();

        // 요청보낼당시 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        String searchDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);
        log.info("baseDate: {}", searchDate);


        try {
//           http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?sidoName=서울&pageNo=1&numOfRows=100&returnType=xml&serviceKey=서비스키&ver=1.0
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty");
            urlBuilder.append("?" + URLEncoder.encode("sidoName", "UTF-8") + "=" + URLEncoder.encode(region, "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8") );
            urlBuilder.append("&" +  URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8") );
            urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=xslbsjNPICkf%2B2T2G6bme0BihSfnUHKHH4DmLkpTwudydktk7uUrVTvyu72ZUQQzU%2FwlUAA5tFoieiHXwVKZBA%3D%3D"); /*서비스명 (대소문자 구분 필수입니다.)*/
            urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode("1.0", "UTF-8") );
//            urlBuilder.append("&" + URLEncoder.encode("searchDate", "UTF-8") + "=" + URLEncoder.encode(searchDate, "UTF-8") );
//            urlBuilder.append("&" + URLEncoder.encode("informCode", "UTF-8") + "=" + URLEncoder.encode("PM25", "UTF-8") );


            log.info("완성된 URL: {}", urlBuilder.toString());

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;

            // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
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

//            System.out.println(sb.toString());


            String jsonString = sb.toString();

            JSONParser parser = new JSONParser();

            // String 객체를 JSON 객체로 변경해 줌.
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

            // "response" 라는 이름의 키에 해당하는 JSON 데이터를 가져옵니다.
            JSONObject response = (JSONObject) jsonObject.get("response");

            // response 안에서 body 키에 해당하는 JSON 데이터를 가져옵니다.
            JSONObject body = (JSONObject) response.get("body");

            // body 에서 items 를 꺼내세요.
            JSONArray itemArray = (JSONArray) body.get("items");


            double totalPm25 = 0;
            int count = 0;

            // 반복문을 이용해서 객체를 하나씩 취득한 후에 원하는 로직을 작성합니다.
            for (Object obj : itemArray) {

                // Object 를 JSON 객체로 변환
                JSONObject item = (JSONObject) obj;


                String stationName = (String) item.get("sidoName");


                // pm25Value 값을 추출
                String pm25ValueStr = (String) item.get("pm25Value");
                if (pm25ValueStr != null && !pm25ValueStr.equals("-")) {
                    double pm25Value = Double.parseDouble(pm25ValueStr);
                    totalPm25 += pm25Value;
                    count++;
                }

            }

            // 평균 계산 및 등급 판정
            if (count == 0) {
                return "데이터 없음";
            }

            // 평균 계산
            double averagePm25 = totalPm25 / count;

            log.info("지역 미세먼지 농도 평균: {}", averagePm25);

            String grade;
            if (averagePm25 <= 15) {
                grade = "좋음";
            } else if (averagePm25 <= 35) {
                grade = "보통";
            } else if (averagePm25 <= 75) {
                grade = "나쁨";
            } else {
                grade = "매우 나쁨";
            }

            // 지역, 미세먼지평균농도, 농도범위


           return grade;
        } catch (Exception e) {
            e.printStackTrace();

            return "오류발생";
        }



    }
}
