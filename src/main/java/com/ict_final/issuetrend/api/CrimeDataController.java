package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.response.CrimeDataResponseDTO;
import com.ict_final.issuetrend.service.CrimeDataService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CrimeData API", description = "지역별 범죄 유형과 발생 현황을 제공하는  api 입니다.")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/issue-trend")
public class CrimeDataController {

    private final CrimeDataService crimeDataService;

    @GetMapping("/api/crime")
    public ResponseEntity<?> getCrimeData(@RequestParam("region") String region) {
        log.info("crimeData GetMapping request!");

        List<CrimeDataResponseDTO> crimeData = crimeDataService.getCrimeData(region);

        return ResponseEntity.ok().body(crimeData);
    }

}
