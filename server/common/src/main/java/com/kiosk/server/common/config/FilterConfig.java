package com.kiosk.server.common.config;

import com.kiosk.server.common.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /**
     * JwtFilter를 등록하는 설정 클래스
     * - 모든 요청 경로에 대해 JwtFilter 적용
     * - 특정 경로는 ('/user', '/user/login') 필터링에서 제외
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean(JwtFilter jwtFilter) {

        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        registrationBean.setName("JwtFilter");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
