package com.kiosk.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final ServiceUrlProperties serviceUrlProperties;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/user/**", "/auth/**").uri(serviceUrlProperties.userService()))
            .route(r -> r.path("/store/**", "/menu/**", "/category/**").uri(serviceUrlProperties.storeService()))
            .route(r -> r.path("/remote/**").uri(serviceUrlProperties.remoteService()))
            .build();
    }
}
