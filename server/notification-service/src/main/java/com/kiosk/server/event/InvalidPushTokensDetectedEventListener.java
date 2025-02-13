package com.kiosk.server.event;

import com.kiosk.server.service.DeletePushTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvalidPushTokensDetectedEventListener {

    private final DeletePushTokensService deletePushTokensService;

    @Async
    @EventListener(InvalidPushTokensDetectedEvent.class)
    public void handle(InvalidPushTokensDetectedEvent event) {
        deletePushTokensService.doService(event.invalidTokens());
        log.info("Removing invalid tokens: count {}", event.invalidTokens().size());
    }
}
