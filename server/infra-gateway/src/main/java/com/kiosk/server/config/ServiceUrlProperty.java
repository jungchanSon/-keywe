package com.kiosk.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gateway.url")
public record ServiceUrlProperty(
    String userService,
    String storeService
) {
}
