package com.kiosk.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${url.user_service}")
    String USER_SERVICE_URL;

    @Value("${url.store_service}")
    String STORE_SERVICE_URL;


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/user/**", "/auth/**", "/user/swagger-ui/**", "/user/v3/api-docs").uri(USER_SERVICE_URL))
            .route(r -> r.path("/store/**", "/menu/**", "/category/**", "/store/swagger-ui/**", "/store/v3/api-docs").uri(STORE_SERVICE_URL))
            .build();
    }
}
