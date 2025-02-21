package com.kiosk.server.service;

public interface UnregisterFcmTokenService {

    void doService(Long profileId, String deviceId);
}
