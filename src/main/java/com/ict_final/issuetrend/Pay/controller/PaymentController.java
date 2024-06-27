package com.ict_final.issuetrend.Pay.controller;

import com.ict_final.issuetrend.Pay.request.PaymentRequestDTO;
import com.ict_final.issuetrend.Pay.response.PaymentApprovalResponseDTO;
import com.ict_final.issuetrend.Pay.response.PaymentResponseDTO;
import com.ict_final.issuetrend.Pay.service.PaymentService;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ready")
    public PaymentResponseDTO readyPayment(@RequestBody PaymentRequestDTO requestDTO) {
        return paymentService.readyPayment(requestDTO);
    }

    @GetMapping("/approve/{userNum}")
    public ResponseEntity<Object> approvePayment(
            @PathVariable Long userNum,
            @RequestParam("pg_token") String pgToken) {
        log.info("userNo: {}, pgToken: {}", userNum, pgToken);
        paymentService.approvePayment(userNum, pgToken);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:3000/home")).build();
    }

//    @PostMapping("/subscribe/{userNo}")
//    public ResponseEntity<?> subscribePayment(@PathVariable Long userNo) {
//        log.info("userNo: {}", userNo);
//        PaymentApprovalResponseDTO response = paymentService.subscribePayment(userNo);
//        return ResponseEntity.ok().body(response);
//    }

    @GetMapping("/cancel")
    public String cancelPayment() {
        // 결제 취소 처리
        return "Payment canceled";
    }

    @GetMapping("/fail")
    public String failPayment() {
        // 결제 실패 처리
        return "Payment failed";
    }

    @GetMapping("/subscriptionStatus/{userNum}")
    public ResponseEntity<?> getSubscriptionStatus(@PathVariable Long userNum) {
        User user = userRepository.findByUserNo(userNum).orElseThrow();
        boolean isSubscribed = user.isSubscribed();
        return ResponseEntity.ok(Map.of("isSubscribed", isSubscribed));
    }

    @PostMapping("/cancelSubscription/{userNum}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long userNum) {
        User user = userRepository.findByUserNo(userNum).orElseThrow();
        user.setSubscribed(false);
        user.setTid(null);
        userRepository.save(user);
        return ResponseEntity.ok("Subscription cancelled successfully");
    }
}
