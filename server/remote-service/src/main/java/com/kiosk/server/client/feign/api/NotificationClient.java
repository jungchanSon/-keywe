package com.kiosk.server.client.feign.api;

import com.kiosk.server.client.feign.dto.SendFcmMessageRequest;
import com.kiosk.server.client.feign.dto.SendFcmMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "${service.notification}")
public interface NotificationClient {

    @PostMapping("/internal/notification/messages")
    ResponseEntity<SendFcmMessageResponse> sendFcmMessage(@RequestBody SendFcmMessageRequest request);
}
