package com.ict_final.issuetrend.api;


import com.ict_final.issuetrend.service.DustService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Dust API", description = "지역별 미세먼지 정보 api 입니다.")
@RestController
@RequestMapping("/dustInfo")
@Slf4j
@RequiredArgsConstructor
public class DustApiController {

    private final DustService dustService;

    @Operation(summary = "지역별 미세먼지 정보 조회", description = "미세먼지 정보 취득을 담당하는 메서드 입니다.")
    @Parameter(name = "region", description = "지역명을 작성하세요", example = "서울", required = true)
    @GetMapping("/dustApi/{region}")
    public ResponseEntity<?> apiRequest(@PathVariable("region") String region) {
        log.info("/dustApi 호출됨!");

        Map<String, String> regionDust = dustService.getRegionDust(region);


        return ResponseEntity.ok().body(regionDust);

    }

}
