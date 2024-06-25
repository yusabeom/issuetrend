package com.ict_final.issuetrend.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dustInfo")
@Slf4j
@RequiredArgsConstructor
public class DustApiController {

    private final com.ict_final.issuetrend.service.dustService dustService;


    @GetMapping("/dustApi/{region}")
    public ResponseEntity<String> apiRequest(@PathVariable("region") String region) {
        log.info("/dustApi 호출됨!");

        String regionDust = dustService.getRegionDust(region);


        return ResponseEntity.ok().body(regionDust);

    }

}
