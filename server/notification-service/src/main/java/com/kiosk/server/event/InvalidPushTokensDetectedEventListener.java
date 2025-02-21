package com.kiosk.server.event;

import com.kiosk.server.domain.PushToken;
import com.kiosk.server.service.DeletePushTokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvalidPushTokensDetectedEventListener {

    private final DeletePushTokensService deletePushTokensService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener(InvalidPushTokensDetectedEvent.class)
    public void handle(InvalidPushTokensDetectedEvent event) {
        List<PushToken> invalidTokens = event.invalidTokens();
        if (invalidTokens == null || invalidTokens.isEmpty()) {
            log.info("No invalid tokens to remove");
            return;
        }

        try {
            log.info("Starting to remove invalid tokens: count {}", invalidTokens.size());
            deletePushTokensService.doService(invalidTokens);
            log.info("Successfully removed invalid tokens: count {}", invalidTokens.size());
        } catch (Exception e) {
            log.error("Failed to remove invalid tokens: count {}", invalidTokens.size(), e);
        }
    }
}
