package com.kiosk.server.controller;

import com.kiosk.server.dto.SendFcmMessageRequest;
import com.kiosk.server.dto.SendFcmMessageResult;
import com.kiosk.server.service.SendFcmMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final SendFcmMessageService sendFcmMessageService;

    @PostMapping("/notification/messages")
    public ResponseEntity<SendFcmMessageResult> sendFcmMessage(@RequestBody SendFcmMessageRequest request) {
        log.info("Trying to send fcm message - title: {}", request.message().title());

        SendFcmMessageResult result = sendFcmMessageService.doService(
            request.targetType(),
            request.targetId(),
            request.message());

        return ResponseEntity.ok(result);
    }
}
