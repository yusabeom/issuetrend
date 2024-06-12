package com.ict_final.issuetrend.api;

import com.ict_final.issuetrend.dto.request.UserSignUpRequestDTO;
import com.ict_final.issuetrend.dto.response.UserSignUpResponseDTO;
import com.ict_final.issuetrend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/issue-trend")
public class UserController {
    private final UserService userService;
    // 이메일 중복 확인 요청 처리
    @GetMapping("/check")
    public ResponseEntity<?> check(String email) {
        log.info("Received email check request for: {}", email);
        if (email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("이메일이 존재하지 않습니다.");
        }
        boolean resultFlag = userService.isDuplicate(email);
        log.info("Email duplication check result: {}", resultFlag);
        return ResponseEntity.ok().body(resultFlag);
    }

    // 회원가입 요청 처리
    @PostMapping
    public ResponseEntity<?> signUp(
            @Validated @RequestPart("user") UserSignUpRequestDTO dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            BindingResult result
    ) {
        log.info("/api/auth POST! - {}", dto);
        ResponseEntity<FieldError> resultEntity = getFieldErrorResponseEntity(result);
        if (resultEntity != null) return resultEntity;
        try {
            String uploadedFilePath = null;
            if (profileImage != null) {
                log.info("attached file name: {}", profileImage.getOriginalFilename());
                // 전달받은 프로필 이미지를 먼저 지정된 경로에 저장한 후 저장 경로를 DB에 세팅하자.
                uploadedFilePath = userService.uploadProfileImage(profileImage);
            }
            UserSignUpResponseDTO responseDTO = userService.create(dto, uploadedFilePath);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // BindingResult 에서 유효성 검사 오류가 있는지 확인
    private static ResponseEntity<FieldError> getFieldErrorResponseEntity(BindingResult result) {
        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }
        return null;
    }
}
