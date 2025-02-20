package com.kiosk.server.service.impl;

import com.kiosk.server.domain.PushToken;
import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.service.RegisterFcmTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterFcmTokenServiceImpl implements RegisterFcmTokenService {

    private final PushTokenRepository pushTokenRepository;

    @Override
    @Transactional
    public void doService(Long userId, Long profileId, String deviceId, String fcmToken) {
        PushToken pushToken = pushTokenRepository.findByDeviceId(deviceId)
            .orElse(new PushToken(userId, profileId, deviceId, fcmToken));

        pushToken.updateToken(fcmToken);

        pushTokenRepository.save(pushToken);

        log.info("Successfully registered fcm token: userId - {} / profileId - {} / fcmToken - {}",
            userId, profileId, fcmToken);
    }
}
