package com.kiosk.server.event;

import com.kiosk.server.client.feign.api.NotificationClient;
import com.kiosk.server.client.feign.dto.NotificationMessage;
import com.kiosk.server.client.feign.dto.SendFcmMessageRequest;
import com.kiosk.server.client.feign.dto.SendFcmMessageResponse;
import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.websocket.message.RemoteOrderResponseMessage;
import com.kiosk.server.websocket.message.RemoteOrderResponseMessageType;
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
public class RemoteOrderHelpRequestedEventListener {

    private final NotificationClient notificationClient;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    @EventListener
    public void handleRemoteOrderHelpRequested(RemoteOrderHelpRequestedEvent event) {
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

        RemoteOrderResponseMessage responseMessage;

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null ||
            response.getBody().successCount() == 0) {
            responseMessage = RemoteOrderResponseMessage.builder()
                .type(RemoteOrderResponseMessageType.ERROR)
                .data(Map.of("message", "가족에게 요청 전송이 실패했어요."))
                .build();
        } else {
            SendFcmMessageResponse result = response.getBody();

            responseMessage = RemoteOrderResponseMessage.builder()
                .type(RemoteOrderResponseMessageType.WAITING)
                .sessionId(session.getSessionId())
                .data(Map.of("success", result.successCount(), "failure", result.failureCount()))
                .build();
        }

        messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), responseMessage);
    }
}
