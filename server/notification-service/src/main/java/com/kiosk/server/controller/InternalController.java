package com.kiosk.server.controller;

import com.kiosk.server.dto.SendEmailRequest;
import com.kiosk.server.dto.SendFcmMessageRequest;
import com.kiosk.server.dto.SendFcmMessageResult;
import com.kiosk.server.service.EmailSender;
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
@RequestMapping("/internal/notification")
public class InternalController {

    private final EmailSender emailSender;
    private final SendFcmMessageService sendFcmMessageService;

    @PostMapping("/messages")
    public ResponseEntity<SendFcmMessageResult> sendFcmMessage(@RequestBody SendFcmMessageRequest request) {
        log.info("Trying to send fcm message - title: {}", request.message().title());

        SendFcmMessageResult result = sendFcmMessageService.doService(
            request.targetType(),
            request.targetIds(),
            request.message());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/emails")
    public ResponseEntity<Void> sendEmail(@RequestBody SendEmailRequest request) {
        try {
            emailSender.send(request.to(), request.subject(), request.content());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to send email to: {}", request.to(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
