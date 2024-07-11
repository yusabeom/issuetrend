package com.ict_final.issuetrend.Pay.service;

import com.ict_final.issuetrend.Pay.request.PaymentRequestDTO;
import com.ict_final.issuetrend.Pay.response.PaymentApprovalResponseDTO;
import com.ict_final.issuetrend.Pay.response.PaymentResponseDTO;
import com.ict_final.issuetrend.entity.User;
import com.ict_final.issuetrend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    @Value("${pay.admin-key}")
    private String kakaoApiKey;

    private String baseURL = "";

    @Autowired
    private UserRepository userRepository;

    public PaymentResponseDTO readyPayment(@RequestBody PaymentRequestDTO requestDTO) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromUriString("https://open-api.kakaopay.com/online/v1/payment/ready")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoApiKey);
        headers.set("Content-type", "application/json");

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME"); // 테스트용 가맹점 코드
        params.put("partner_order_id", "subs"+requestDTO.getUserNo());
        params.put("partner_user_id", String.valueOf(requestDTO.getUserNo()));
        params.put("item_name", requestDTO.getItemName());
        params.put("quantity", String.valueOf(requestDTO.getQuantity()));
        params.put("total_amount", String.valueOf(requestDTO.getTotalAmount()));
        params.put("vat_amount", "0");
        params.put("tax_free_amount", "0");
        params.put("approval_url", "http://13.209.56.91/payment/approve/"+requestDTO.getUserNo());
        params.put("cancel_url", "http://13.209.56.91/payment/cancel");
        params.put("fail_url", "http://13.209.56.91/payment/fail");

        log.info("Request Parameters: {}", params);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        if (response == null) {
            throw new IllegalStateException("Response from Kakao API is null");
        }

        PaymentResponseDTO responseDTO = new PaymentResponseDTO();
//        responseDTO.setTid((String) response.get("tid"));
        User user = userRepository.findByUserNo(requestDTO.getUserNo()).orElseThrow();
        user.setTid((String) response.get("tid"));
        userRepository.save(user);
        responseDTO.setNextRedirectPcUrl((String) response.get("next_redirect_pc_url"));

        return responseDTO;
    }

    public PaymentApprovalResponseDTO approvePayment(Long userNo, String pgToken) {
        User founduser = userRepository.findByUserNo(userNo).orElseThrow();
        String tid = founduser.getTid();

        RestTemplate restTemplate = new RestTemplate();
        URI uri = UriComponentsBuilder.fromUriString("https://open-api.kakaopay.com/online/v1/payment/approve")
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoApiKey);
        headers.set("Content-type", "application/json");

        Map<String, String> params = new HashMap<>();
        params.put("cid", "TC0ONETIME"); // 테스트용 cid TC0ONETIME
        params.put("tid", tid); // 실제 tid
        params.put("partner_order_id", "subs"+userNo);
        params.put("partner_user_id", userNo.toString());
        params.put("pg_token", pgToken);

        log.info("Approve Request Parameters: {}", params);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
        Map<String, Object> response = responseEntity.getBody();

        if (response == null) {
            throw new IllegalStateException("Response from Kakao API is null");
        }


        User user = userRepository.findByUserNo(userNo).orElseThrow();
        user.setSubscribed(true);
        userRepository.save(user);
        return new PaymentApprovalResponseDTO(tid, "구독 서비스", 9900);
    }

//    public PaymentApprovalResponseDTO subscribePayment(Long userNo) {
//        User founduser = userRepository.findByUserNo(userNo).orElseThrow();
//        String tid = founduser.getTid();
//        String sid = founduser.getSid();
//
//        RestTemplate restTemplate = new RestTemplate();
//        URI uri = UriComponentsBuilder.fromUriString("https://open-api.kakaopay.com/online/v1/payment/subscription")
//                .build().toUri();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "SECRET_KEY " + kakaoApiKey);
//        headers.set("Content-type", "application/json");
//
//        Map<String, String> params = new HashMap<>();
//        params.put("cid", "TCSUBSCRIP"); // 테스트용 cid
//        params.put("sid", sid); // 실제 sid
//        params.put("partner_order_id", "subs"+userNo);
//        params.put("partner_user_id", userNo.toString());
//        params.put("item_name", "구독 서비스");
//        params.put("quantity", "1");
//        params.put("total_amount", "990");
//        params.put("tax_free_amount", "0");
//
//        log.info("Approve Request Parameters: {}", params);
//
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);
//        ResponseEntity<Map> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, entity, Map.class);
//        Map<String, Object> response = responseEntity.getBody();
//
//        if (response == null) {
//            throw new IllegalStateException("Response from Kakao API is null");
//        }
//        return new PaymentApprovalResponseDTO(tid, sid, "구독 서비스", 990);
//    }
}