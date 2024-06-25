package com.ict_final.issuetrend.api;


import com.ict_final.issuetrend.service.DustService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dustInfo")
@Slf4j
@RequiredArgsConstructor
public class DustApiController {

    private final DustService dustService;


    @GetMapping("/dustApi/{region}")
    public ResponseEntity<?> apiRequest(@PathVariable("region") String region) {
        log.info("/dustApi 호출됨!");

        Map<String, String> regionDust = dustService.getRegionDust(region);


        return ResponseEntity.ok().body(regionDust);

    }

}
