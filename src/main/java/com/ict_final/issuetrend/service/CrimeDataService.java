package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.response.CrimeDataResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CrimeDataService {

    public List<CrimeDataResponseDTO> getCrimeData(String region) {

        try {
            StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/3074462/v1/uddi:fe3ae686-8f7d-4d82-8c3a-901a02a0aa75"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("page", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("perPage", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("100", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            urlBuilder.append("&" + URLEncoder.encode("returnType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8)); /*요청자료형식(XML/JSON) Default: XML*/
            urlBuilder.append("&" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=de60eL5PMfARm%2FG4LejBXWtjMGLrsj7cxEGK6VRlV45REpSIwSddg25LBDg9jPe%2B4uKK6V3uvgjf6%2FoGYT74xw%3D%3D"); /*Service Key*/

            log.info("완성된 URL : {}", urlBuilder);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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

            String jsonString = sb.toString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
            JSONArray dataArray = (JSONArray) jsonObject.get("data");

            List<CrimeDataResponseDTO> dtoList = new ArrayList<>();
            for (Object obj : dataArray) {
                JSONObject dataObject = (JSONObject) obj;

                CrimeDataResponseDTO dto = findKeysStartWith(dataObject, region);
                dtoList.add(dto);
            }

            return dtoList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private CrimeDataResponseDTO findKeysStartWith(JSONObject dataObject, String region) {
        String category = (String) dataObject.get("범죄중분류");
        Long frequency = 0L;

        for (Object key : dataObject.keySet()) {
            String keyStr = (String) key;
            if (keyStr.startsWith(region)) {
                frequency += (Long) dataObject.get(keyStr);
            }
        }
        return new CrimeDataResponseDTO(category, frequency);

    }




}
