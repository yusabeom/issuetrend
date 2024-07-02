package com.ict_final.issuetrend.config;

import com.ict_final.issuetrend.filter.JWTExceptionFilter;
import com.ict_final.issuetrend.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 자동 권한 검사를 컨트롤러의 메서드에서 전역적으로 수행하기 위한 설정.
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final JWTExceptionFilter jwtExceptionFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final RequestProperties properties;

    //Spring Security 설정을 통해 특정 엔드포인트에 대한 접근을 허용하고, 나머지 요청은 인증을 요구하며, CSRF 보호를 비활성화합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    jwtAuthFilter.setPermitAllPatterns(properties.getPermitAllPatterns());
        log.info("리스트: {}", properties.getPermitAllPatterns());
//        log.info("배열로 변환: {}", Arrays.toString(properties.getPermitAllPatterns().toArray()).substring(1, ));

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .cors(Customizer.withDefaults())
                // 세션 관리 상태를 STATELESS로 설정해서 spring security가 제공하는 세션 생성 및 관리 기능 사용하지 않겠다.
                .sessionManagement(SessionManagement ->
                        SessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .authorizeHttpRequests(auth -> {
                    log.info("properties.getPermitAllPatterns(): {}", properties.getPermitAllPatterns());
                            properties.getPermitAllPatterns()
                                    .forEach(url -> auth.requestMatchers(url).permitAll());
                            // /issue-trend/** 엔드포인트에 대한 접근을 허용합니다.
                            auth.requestMatchers("/issue-trend/load-profile").authenticated();
                            auth.requestMatchers("/issue-trend/password-check").authenticated();
                            auth.requestMatchers("/issue-trend/update-my-info").authenticated();
                            auth.requestMatchers("/issue-trend/delete").authenticated();
                    auth.requestMatchers("/issue-trend/logout").authenticated();


                            auth.anyRequest().authenticated(); // 그 외의 모든 요청은 인증 필요
                        }


                )
                .exceptionHandling(ExceptionHandling -> {
                    ExceptionHandling.accessDeniedHandler(accessDeniedHandler);
                });

        return http.build();
    }

    // 비밀번호 암호화 객체를 빈 등록
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}