package com.kiosk.server.service.impl;

import com.kiosk.server.domain.PushToken;
import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.service.RegisterFcmTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterFcmTokenServiceImpl implements RegisterFcmTokenService {

    private final PushTokenRepository pushTokenRepository;

    @Override
    @Transactional
    public void doService(Long userId, Long profileId, String deviceId, String fcmToken) {
        log.info("RegisterFcmTokenService executed : userId {} / profileId {} / deviceId {} / fcmToken {}", userId, profileId, deviceId, fcmToken);
        Optional<PushToken> optionalPushToken = pushTokenRepository.findByDeviceId(deviceId);
        PushToken pushToken;

        if (optionalPushToken.isEmpty()){
            log.info("기존 디바이스 등록 토큰 없음");
            pushToken = new PushToken(userId, profileId, deviceId, fcmToken);
        } else {
            pushToken = optionalPushToken.get();
            log.info("기존 디바이스 등록 토큰 발견 - token: {}", pushToken.getToken());
        }

        pushToken.updateToken(fcmToken);

        pushTokenRepository.save(pushToken);

        log.info("Successfully registered fcm token: userId - {} / profileId - {} / fcmToken - {}",
            userId, profileId, fcmToken);
    }
}
