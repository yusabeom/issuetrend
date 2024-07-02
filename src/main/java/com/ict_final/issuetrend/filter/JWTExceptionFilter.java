package com.ict_final.issuetrend.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict_final.issuetrend.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JWTExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            // 예외가 발생하지 않으면 필터를 통과~
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었을 시 Auth Filter에서 예외가 발생 -> 앞에 있는 Exception Filter로 전달.
            log.warn("ExpiredJwtException 발생함!");
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
        } catch (JwtException e) {
            log.warn("JwtException 발생함!");
            e.printStackTrace();
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.warn("토큰이 전달되지 않음!");
            setErrorResponse(response, ErrorCode.INVALID_AUTH);
        } catch (Exception e) {
            log.warn("알 수 없는 예외 발생!");
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        log.info("setErrorResponse called!");

        // Map 생성 및 데이터 추가
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", errorCode.toString());
        responseMap.put("code", errorCode.getHttpStatus());

        // Map을 JSON 문자열로 변환
        String jsonString = new ObjectMapper().writeValueAsString(responseMap);

        // JSON 데이터를 응답객체에 실어서 브라우저로 바로 응답.
        response.getWriter().write(jsonString);
    }


}
















