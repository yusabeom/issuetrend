package com.ict_final.issuetrend.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//@ControllerAdvice
// RestController에서 발생되는 예외를 전역적으로 처리할 수 있게 하는 아노테이션.
// 예외 상황에 따른 응답을 REST 방식으로 클라이언트에게 전달할 수 있다.
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler({RuntimeException.class, NoRegisteredArgumentException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        log.info("handleIllegalArgumentException 호출!");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}











