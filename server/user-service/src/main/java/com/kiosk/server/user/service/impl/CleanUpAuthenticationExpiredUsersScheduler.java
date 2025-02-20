package com.kiosk.server.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CleanUpAuthenticationExpiredUsersScheduler {

    private final CleanUpAuthenticationExpiredUsersService cleanUpService;

    @Scheduled(fixedRate = 1800000)
    public void cleanup() {
        log.info("CleanUpAuthenticationExpiredUsersScheduler started");
        cleanUpService.execute();
        log.info("CleanUpAuthenticationExpiredUsersScheduler ended");
    }
}
