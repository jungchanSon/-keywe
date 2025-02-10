package com.kiosk.server.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.kiosk.server.client.feign.api")
public class FeignConfig {
}
