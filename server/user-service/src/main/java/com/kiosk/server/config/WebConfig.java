package com.kiosk.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final AuthenticationInterceptor authenticationInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationInterceptor)
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/auth/user/login", "/auth/user/kiosk-login", "/internal/**", "/user/**",
                "/swagger-ui/**", "/user/v3/api-docs/**");

        registry.addInterceptor(authorizationInterceptor)
            .order(2)
            .addPathPatterns("/**")
            .excludePathPatterns("/user", "/auth/**", "/internal/**", "/user/swagger-ui/**", "/user/v3/api-docs");
    }
}
