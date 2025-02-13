package com.kiosk.server.event;

import com.kiosk.server.client.feign.api.NotificationClient;
import com.kiosk.server.client.feign.dto.NotificationMessage;
import com.kiosk.server.client.feign.dto.SendFcmMessageRequest;
import com.kiosk.server.client.feign.dto.SendFcmMessageResponse;
import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import com.kiosk.server.websocket.message.response.RemoteOrderResponseType;
import com.kiosk.server.websocket.message.response.RemoteOrderError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RemoteOrderRequestedEventListener {

    private final NotificationClient notificationClient;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void handleRemoteOrderHelpRequested(RemoteOrderRequestedEvent event) {
        RemoteOrderSession session = event.session();

        // FCM 메시지 구성 로직
        Map<String, String> notificationData = Map.of(
            "sessionId", session.getSessionId(),
            "storeId", session.getStoreId(),
            "kioskUserId", session.getKioskUserId()
        );

        NotificationMessage notificationMessage = new NotificationMessage(
            "대리 주문 도움 요청",
            "키오스크 주문 도움이 필요합니다.",
            "REMOTE-ORDER",
            notificationData,
            NotificationMessage.MessagePriority.HIGH,
            30
        );

        SendFcmMessageRequest request = new SendFcmMessageRequest(
            "USER",
            Long.valueOf(session.getFamilyId()),
            notificationMessage
        );

        ResponseEntity<SendFcmMessageResponse> response = notificationClient.sendFcmMessage(request);

        RemoteOrderResponse responseMessage;

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null ||
            response.getBody().successCount() == 0) {
            responseMessage = RemoteOrderResponse.error(RemoteOrderError.PUSH_NOTIFICATION_SEND_FAILED);
        } else {
            SendFcmMessageResponse result = response.getBody();
            responseMessage = RemoteOrderResponse.success(
                RemoteOrderResponseType.WAITING,
                Map.of("success", result.successCount(), "failure", result.failureCount())
            );
        }

        messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), responseMessage);
    }
}
