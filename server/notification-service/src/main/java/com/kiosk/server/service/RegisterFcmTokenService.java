package com.kiosk.server.service;

public interface RegisterFcmTokenService {

    void doService(Long userId, Long profileId, String deviceId, String fcmToken);
}
