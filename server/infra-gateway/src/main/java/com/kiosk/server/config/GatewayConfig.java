package com.kiosk.server.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final ServiceUrlProperty serviceUrlProperty;

    public GatewayConfig(ServiceUrlProperty serviceUrlProperty) {
        this.serviceUrlProperty = serviceUrlProperty;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r.path("/user/**").uri(serviceUrlProperty.userService()))
            .route(r -> r.path("/store/**", "/menu/**", "/category/**").uri(serviceUrlProperty.storeService()))
            .build();
    }
}
