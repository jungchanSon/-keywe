package com.kiosk.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration commonConfig = new CorsConfiguration();
        commonConfig.setAllowedOriginPatterns(List.of("http://localhost:8080/**", "http://i12a404.p.ssafy.io:8080/**"));
        commonConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        commonConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        commonConfig.setAllowCredentials(true);

        CorsConfiguration emailVerificationConfig = new CorsConfiguration();
        emailVerificationConfig.setAllowedOriginPatterns(List.of("*"));  // 모든 출처 허용
        emailVerificationConfig.setAllowedMethods(List.of("GET"));       // GET 메서드만 허용
        emailVerificationConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        emailVerificationConfig.setAllowCredentials(false);              // credentials 불필요

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/swagger-ui/**", commonConfig);
        source.registerCorsConfiguration("/v3/api-docs/**", commonConfig);
        source.registerCorsConfiguration("/auth/verify-email", emailVerificationConfig);

        return new CorsWebFilter(source);
    }
}
