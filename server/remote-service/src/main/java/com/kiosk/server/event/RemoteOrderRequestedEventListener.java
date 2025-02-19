package com.kiosk.server.event;

import com.kiosk.server.client.feign.api.NotificationClient;
import com.kiosk.server.client.feign.dto.NotificationMessage;
import com.kiosk.server.client.feign.dto.SendFcmMessageRequest;
import com.kiosk.server.client.feign.dto.SendFcmMessageResponse;
import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.domain.RemoteOrderSessionRepository;
import com.kiosk.server.websocket.exception.RemoteOrderError;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import com.kiosk.server.websocket.message.response.RemoteOrderResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RemoteOrderRequestedEventListener {

    private final NotificationClient notificationClient;
    private final SimpMessagingTemplate messagingTemplate;
    private final RemoteOrderSessionRepository remoteOrderSessionRepository;

    @Async
    @EventListener
    public void handleRemoteOrderHelpRequested(RemoteOrderRequestedEvent event) {
        RemoteOrderSession session = event.session();
        String kioskUserName = session.getKioskUserName();

        // FCM 메시지 구성 로직
        Map<String, String> notificationData = Map.of(
            "title", "대리 주문 도움 요청",
            "body", kioskUserName + "님이 키오스크 주문 도움을 요청했습니다",
            "sessionId", session.getSessionId(),
            "storeId", session.getStoreId(),
            "kioskUserId", session.getKioskUserId(),
            "kioskUserName", kioskUserName
        );

        NotificationMessage notificationMessage = new NotificationMessage(
            "대리 주문 도움 요청",
            kioskUserName + "님이 키오스크 주문 도움을 요청했습니다",
            "REMOTE-ORDER",
            notificationData,
            NotificationMessage.MessagePriority.HIGH,
            30
        );

        Map<String, String> helperIdNameMap = remoteOrderSessionRepository.findHelperIdNameMap(session.getFamilyId());
        List<String> helperIds = helperIdNameMap.keySet().stream().toList();

        SendFcmMessageRequest request = new SendFcmMessageRequest("PROFILE", helperIds, notificationMessage);

        ResponseEntity<SendFcmMessageResponse> response = notificationClient.sendFcmMessage(request);

        if (
            response.getStatusCode() != HttpStatus.OK ||
                response.getBody() == null ||
                response.getBody().successCount() == 0
        ) {
            messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(),
                RemoteOrderResponse.error(RemoteOrderError.PUSH_NOTIFICATION_SEND_FAILED));
        } else {
            SendFcmMessageResponse result = response.getBody();

            RemoteOrderResponse responseMessage = RemoteOrderResponse.success(
                RemoteOrderResponseType.WAITING,
                Map.of(
                    "success", result.successCount(),
                    "failure", result.failureCount()
                )
            );

            messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), responseMessage);
        }
    }
}
