package com.kiosk.server.websocket.handler;

import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.service.AgoraService;
import com.kiosk.server.service.RemoteOrderService;
import com.kiosk.server.service.dto.AgoraChannelInfo;
import com.kiosk.server.websocket.message.request.RemoteOrderAcceptRequest;
import com.kiosk.server.websocket.message.request.RemoteOrderEndRequest;
import com.kiosk.server.websocket.message.request.RemoteOrderRequest;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import com.kiosk.server.websocket.message.response.RemoteOrderResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
        RemoteOrderRequest requestMessage,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");
        String sessionId = remoteOrderService.createSession(userId, familyId, requestMessage.storeId());

        // 키오스크에게 sessionId 전달
        RemoteOrderResponse responseMessage = RemoteOrderResponse.success(
            RemoteOrderResponseType.REQUESTED,
            Map.of("sessionId", sessionId)
        );

        messagingTemplate.convertAndSend("/topic/" + userId, responseMessage);
    }

    @MessageMapping("/remote-order/accept")
    public void handleOrderAccept(
        RemoteOrderAcceptRequest message,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String helperUserId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");

        RemoteOrderSession session = remoteOrderService.acceptSession(message.sessionId(), helperUserId, familyId);

        // Agora 채널 생성 및 전송
        AgoraChannelInfo agoraChannelInfo = agoraService.createChannel(session.getSessionId());

        // 양쪽에 Agora 토큰 전달
        RemoteOrderResponse responseMessage = RemoteOrderResponse.success(
            RemoteOrderResponseType.ACCEPTED,
            Map.of(
                "sessionId", session.getSessionId(),
                "helperUserId", helperUserId,
                "kioskUserId", session.getKioskUserId(),
                "channel", agoraChannelInfo
            )
        );

        messagingTemplate.convertAndSend("/topic/" + helperUserId, responseMessage);
        messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), responseMessage);
    }

    @MessageMapping("/remote-order/end")
    public void handleOrderEnd(
        RemoteOrderEndRequest message,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");

        RemoteOrderSession session = remoteOrderService.endSession(userId, message.sessionId());

        RemoteOrderResponse endMessage = RemoteOrderResponse.success(RemoteOrderResponseType.END);

        // 상대방에게 이벤트 전달
        String targetUserId = userId.equals(session.getKioskUserId())
            ? session.getHelperUserId()
            : session.getKioskUserId();

        messagingTemplate.convertAndSend("/topic/" + targetUserId, endMessage);
    }
}
