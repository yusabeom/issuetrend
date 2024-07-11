package com.ict_final.issuetrend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
      log.info("/health -> GET: Server is healthy now...");
      return ResponseEntity.ok().body("OK");
    }

}
