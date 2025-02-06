package com.kiosk.server.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Route {

    @Value("${GATEWAY.USER_SERVICE_URL}")
    String USER_SERVICE_URL;

    @Value("${GATEWAY.STORE_SERVICE_URL}")
    String STORE_SERVICE_URL;

    @Bean
    public RouteLocator cRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        r -> r.path("/user/**")
                                .uri(USER_SERVICE_URL)
                )
                .route(
                        r -> r.path("/store/**")
                                .uri(STORE_SERVICE_URL)
                )
                .build();

    }
}
