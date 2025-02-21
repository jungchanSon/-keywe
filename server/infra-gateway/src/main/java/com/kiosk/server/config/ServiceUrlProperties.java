package com.kiosk.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "url")
public record ServiceUrlProperties(
    String userService,
    String storeService,
    String remoteService,
    String orderService,
    String notificationService
) {
}
