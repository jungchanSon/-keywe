package com.kiosk.server.client.feign.api;

import com.kiosk.server.client.feign.dto.SendEmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationService", url = "${service.notification}")
public interface NotificationServiceClient {

    @PostMapping("/internal/notification/emails")
    ResponseEntity<Void> sendEmail(@RequestBody SendEmailRequest request);
}
