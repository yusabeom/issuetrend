package com.ict_final.issuetrend.service;

import com.ict_final.issuetrend.dto.response.NaverImageResponseDto;
import com.ict_final.issuetrend.dto.response.NaverRestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NaverService {

    @Value("${naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    public List<NaverRestResponseDto> searchRestaurant(String query) throws ParseException {

        // 요청 uri : https://openapi.naver.com/v1/search/local.json?query=검색어&display=5&start=1&sort=random
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", query)
                .queryParam("display", 5)
                .queryParam("start", 1)
                .queryParam("sort", "random")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();

        log.info("요청 URI: " + uri.toString());


        // 요청 헤더 설정 (인증키)
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        // 네이버 서버에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);


        // 응답 데이터 받아오기
        String body = responseEntity.getBody();

        // DTO 타입으로 변환하기
        List<NaverRestResponseDto> res = parsingToJson(body);


        return res;

    }

    // 이미지 찾기
    public List<NaverImageResponseDto> searchDishImg(String search) throws ParseException {
        // 요청 uri : https://openapi.naver.com/v1/search/image?query=검색어&display=5&start=1&sort=random
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/image")
                .queryParam("query", search)
                .queryParam("display", 10)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .queryParam("filter", "medium")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();

        log.info("이미지 요청 URI: " + uri.toString());


        // 요청 헤더 설정 (인증키)
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", NAVER_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();

        // 네이버 서버에 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(req, String.class);


        // 응답 데이터 받아오기
        String body = responseEntity.getBody();
//        log.info("image response body is " + body);

        // JSON 파싱
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.parse(body);

        JSONObject jsonObject = (JSONObject) object;

        JSONArray items = (JSONArray) jsonObject.get("items");
//        log.info("*** JSONArray items: " + items);

        List<NaverImageResponseDto> resDto = new ArrayList<>(); // 응답할 데이터
        for (int i = 0; i < 10; i++) {
            JSONObject object1 = (JSONObject) items.get(i);

            // dto에 값을 정제해서 넣기
            NaverImageResponseDto dto = NaverImageResponseDto.builder()
                    .title(object1.getAsString("title"))
                    .link(object1.getAsString("link"))
                    .thumbnail(object1.getAsString("thumbnail"))
                    .sizeheight(object1.getAsString("sizeheight"))
                    .sizewidth(object1.getAsString("sizewidth"))
                    .build();

            resDto.add(dto);
//            log.info("{}번째 DTO: {}", i, dto);
        }
//        log.info("==============================");

//        log.info("DTO List : " + resDto.toString());

        // DTO 타입으로 변환하기
        return resDto;

    }

    // 문자열을 Json으로 파싱하기
    private List<NaverRestResponseDto> parsingToJson(String jsonStr) throws ParseException {

        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.parse(jsonStr);

        JSONObject jsonObject = (JSONObject) object;




//        log.info("==================== jsonObject =======================");
        String lastBuildDate = jsonObject.getAsString("lastBuildDate");
//        log.info("lastBuildDate: " + lastBuildDate);
//        Number total = jsonObject.getAsNumber("total");
//        log.info("total: " + total);


        JSONArray items = (JSONArray) jsonObject.get("items");
        List<NaverRestResponseDto> resDto = new ArrayList<>(); // 응답할 데이터
        for (int i = 0; i < 5; i++) {
            JSONObject object1 = (JSONObject) items.get(i);

            // dto에 값을 정제해서 넣기
            NaverRestResponseDto dto = NaverRestResponseDto.builder()
                    .title(object1.getAsString("title"))
                    .roadAddress(object1.getAsString("roadAddress"))
                    .link(object1.getAsString("link"))
                    .category(object1.getAsString("category"))
                    .address(object1.getAsString("address"))
                    .mapx((Long) object1.getAsNumber("mapx"))
                    .mapy((Long) object1.getAsNumber("mapy"))
                    .build();
            
            resDto.add(dto);
        }

//
//        log.info("items(0): " + items.get(0));
//        log.info("items(1): " + items.get(1));
//
//        Object obj1 = items.get(1);
//        JSONObject object1 = (JSONObject) obj1;
//        log.info("object1: " + object1);
//
//        String roadAddress = object1.getAsString("roadAddress");
//        log.info("roadAddress: " + roadAddress);
//        Number mapx = object1.getAsNumber("mapx");
//        log.info("mapx: " + mapx);

//        Map<String, Object> jsonData = new Map<>();
//        jsonData.put("lastBuildDate", lastBuildDate);
//        jsonData.put("items", resDto);


        return resDto;
    }

   
}
