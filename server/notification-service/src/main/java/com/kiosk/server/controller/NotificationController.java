package com.kiosk.server.controller;

import com.kiosk.server.dto.RegisterTokenRequest;
import com.kiosk.server.service.RegisterFcmTokenService;
import com.kiosk.server.service.UnregisterFcmTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final RegisterFcmTokenService registerFcmTokenService;
    private final UnregisterFcmTokenService unregisterFcmTokenService;

    @PostMapping("/tokens")
    public ResponseEntity<Void> registerToken(
        @RequestHeader("userId") Long userId,
        @RequestHeader("profileId") Long profileId,
        @RequestBody RegisterTokenRequest request
    ) {
        log.info("푸시 토큰 등록 요청 수신");
        registerFcmTokenService.doService(userId, profileId, request.deviceId(), request.token());
        log.info("푸시 토큰 등록 요청 완료");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tokens/{deviceId}")
    public ResponseEntity<Void> unregisterToken(
        @RequestHeader("profileId") Long profileId,
        @PathVariable String deviceId
    ) {
        unregisterFcmTokenService.doService(profileId, deviceId);
        return ResponseEntity.ok().build();
    }
}
