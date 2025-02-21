package com.kiosk.server.service.impl;

import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.service.UnregisterFcmTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnregisterFcmTokenServiceImpl implements UnregisterFcmTokenService {

    private final PushTokenRepository pushTokenRepository;

    @Override
    public void doService(Long profileId, String deviceId) {
        pushTokenRepository.findByDeviceId(deviceId)
            .ifPresent(pushToken -> {
                pushToken.verifyProfileAccess(profileId);
                pushTokenRepository.delete(pushToken);
            });

        log.info("Successfully unregistered fcm token - deviceId {}", deviceId);
    }
}
