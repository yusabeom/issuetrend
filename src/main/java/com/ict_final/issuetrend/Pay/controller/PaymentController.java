package com.ict_final.issuetrend.Pay.controller;

import com.ict_final.issuetrend.Pay.request.PaymentRequestDTO;
import com.ict_final.issuetrend.Pay.response.PaymentResponseDTO;
import com.ict_final.issuetrend.Pay.service.PaymentService;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Tag(name = "Payment API", description = "카카오페이 기반 구독서비스 결제 api 입니다.")
@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "결제 준비", description = "결제 준비를 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "itemName",description = "결제할 품목을 입력하세요", example = "구독 서비스", required = true),
            @Parameter(name = "quantity",description = "총 수량을 입력하세요", example = "1", required = true),
            @Parameter(name = "totalAmount",description = "결제 총액을 입력하세요", example = "99,000", required = true)
    })
    @PostMapping("/ready")
    public PaymentResponseDTO readyPayment(@RequestBody PaymentRequestDTO requestDTO) {
        return paymentService.readyPayment(requestDTO);
    }

    @Operation(summary = "결제 승인", description = "결제 승인을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "userNo", description = "회원 고유 번호를 작성하세요", example = "1", required = true),
            @Parameter(name = "pg_token",description = "승인시 Url에 묻어온 pg_token값을 입력하세요", example = "20byte영문+숫자", required = true),
    })
    @GetMapping("/approve/{userNum}")
    public ResponseEntity<Object> approvePayment(
            @PathVariable Long userNum,
            @RequestParam("pg_token") String pgToken) {
        log.info("userNo: {}, pgToken: {}", userNum, pgToken);
        paymentService.approvePayment(userNum, pgToken);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }

    @Operation(summary = "결제 취소", description = "결제 취소를 담당하는 메서드 입니다.")
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelPayment() {
        // 결제 취소 처리
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }
    @Operation(summary = "결제 실패", description = "결제 실패를 담당하는 메서드 입니다.")
    @GetMapping("/fail")
    public ResponseEntity<?> failPayment() {
        // 결제 실패 처리
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }

    @Operation(summary = "구독 상태 조회", description = "구독 상태 조회를 담당하는 메서드 입니다.")
    @Parameter(name = "userNum", description = "회원 고유 번호를 작성하세요", example = "1", required = true)
    @GetMapping("/subscriptionStatus/{userNum}")
    public ResponseEntity<?> getSubscriptionStatus(@PathVariable Long userNum) {
        User user = userRepository.findByUserNo(userNum).orElseThrow();
        boolean isSubscribed = user.isSubscribed();
        return ResponseEntity.ok(Map.of("isSubscribed", isSubscribed));
    }

    @Operation(summary = "구독 취소", description = "구독 취소를 담당하는 메서드 입니다.")
    @Parameter(name = "userNum", description = "회원 고유 번호를 작성하세요", example = "1", required = true)
    @PostMapping("/cancelSubscription/{userNum}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long userNum) {
        User user = userRepository.findByUserNo(userNum).orElseThrow();
        user.setSubscribed(false);
        user.setTid(null);
        userRepository.save(user);
        return ResponseEntity.ok("Subscription cancelled successfully");
    }
}
