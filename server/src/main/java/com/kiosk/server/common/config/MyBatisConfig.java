package com.kiosk.server.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.kiosk.server.user.domain, com.kiosk.server.store.domain")
public class MyBatisConfig {
}