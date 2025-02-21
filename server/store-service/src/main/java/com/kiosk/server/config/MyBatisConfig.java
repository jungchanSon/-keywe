package com.kiosk.server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.kiosk.server.store.domain, com.kiosk.server.image.repository")
public class MyBatisConfig {
}
