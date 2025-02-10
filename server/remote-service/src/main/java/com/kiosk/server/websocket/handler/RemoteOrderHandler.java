package com.kiosk.server.websocket.handler;

import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.service.AgoraService;
import com.kiosk.server.service.RemoteOrderService;
import com.kiosk.server.websocket.message.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RemoteOrderHandler {

    private final AgoraService agoraService;
    private final RemoteOrderService remoteOrderService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/remote-order/request")
    public void handleOrderRequest(
        RemoteOrderRequestMessage requestMessage,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");
        String sessionId = remoteOrderService.createSession(userId, familyId, requestMessage.storeId());

        if (sessionId == null) {
            messagingTemplate.convertAndSend(
                "/topic/" + userId,
                RemoteOrderResponseMessage.builder()
                    .type(RemoteOrderResponseMessageType.ERROR)
                    .data(Map.of("message", "User does not have parent role"))
                    .build()
            );
            return;
        }

        // 키오스크에게 sessionId 전달
        RemoteOrderResponseMessage responseMessage = RemoteOrderResponseMessage.builder()
            .type(RemoteOrderResponseMessageType.REQUESTED)
            .sessionId(sessionId)
            .timestamp(LocalDateTime.now().toString())
            .build();

        messagingTemplate.convertAndSend("/topic/" + userId, responseMessage);
    }

    @MessageMapping("/remote-order/accept")
    public void handleOrderAccept(
        RemoteOrderAcceptRequestMessage message,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String helperUserId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");

        RemoteOrderSession session = remoteOrderService.acceptSession(message.sessionId(), helperUserId, familyId);

        // 이미 다른 헬퍼가 수락했거나 시간 초과된 경우
        if (session == null) {
            messagingTemplate.convertAndSend(
                "/topic/" + helperUserId,
                RemoteOrderResponseMessage.builder()
                    .type(RemoteOrderResponseMessageType.ERROR)
                    .sessionId(message.sessionId())
                    .data(Map.of("message", "Session already accepted or expired"))
                    .build()
            );
            return;
        }

        // Agora 채널 생성 및 전송
        AgoraChannelInfo channelInfo = agoraService.createChannel(session.getSessionId());

        // 양쪽에 Agora 토큰 전달
        RemoteOrderResponseMessage acceptedMessage = RemoteOrderResponseMessage.builder()
            .type(RemoteOrderResponseMessageType.ACCEPTED)
            .sessionId(session.getSessionId())
            .data(Map.of("kioskUserId", session.getKioskUserId(), "helperUserId", helperUserId))
            .channelInfo(channelInfo)
            .build();

        messagingTemplate.convertAndSend("/topic/" + helperUserId, acceptedMessage);
        messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), acceptedMessage);
    }

    @MessageMapping("/remote-order/end")
    public void handleOrderEnd(
        RemoteOrderEndRequestMessage message,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");

        RemoteOrderSession session = remoteOrderService.endSession(userId, message.sessionId());

        RemoteOrderResponseMessage endMessage = RemoteOrderResponseMessage.builder()
            .type(RemoteOrderResponseMessageType.END)
            .sessionId(session.getSessionId())
            .build();

        // 상대방에게 이벤트 전달
        String targetUserId = userId.equals(session.getKioskUserId())
            ? session.getHelperUserId()
            : session.getKioskUserId();

        messagingTemplate.convertAndSend("/topic/" + targetUserId, endMessage);
    }
}
