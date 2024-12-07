package com.jypark.playlistweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                // 1. 페이지 접근 권한 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // 루트 페이지, 로그인 페이지, 그리고 css와 js파일에 대해 인증 없이 접근 가능( + img, lib)
                                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/lib/**").permitAll()
                                // 그 외 모든 요청은 인증이 필요
                                .anyRequest().authenticated()
                )
                // 2. OAuth2 로그인 설정
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                // 사용자 정의 로그인 페이지 경로 설정
                                .loginPage("/login/login-view").permitAll()
                                // 로그인 성공 시 리디렉션할 경로 설정
                                .defaultSuccessUrl("/main/main-view")
                )
                // 3. 로그아웃 설정(아직 안됨)
                .logout(logout ->
                        logout
                                // 로그아웃 URL 설정
                                .logoutUrl("/logout")
                                // 로그아웃 성공 시 리디렉션할 URL 설정
                                .logoutSuccessUrl("/login/login-view")
                                // 세션 무효화 및 인증정보 삭제
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                // JSESSIONID 쿠키 삭제
                                .deleteCookies("JSESSIONID")
                );
        // 보안 필터 체인을 구성한 후 반환
        return http.build();
    }
}
