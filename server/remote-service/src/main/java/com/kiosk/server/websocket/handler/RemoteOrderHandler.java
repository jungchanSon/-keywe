package com.kiosk.server.websocket.handler;

import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.service.AgoraService;
import com.kiosk.server.service.RemoteOrderService;
import com.kiosk.server.service.dto.AgoraChannelInfo;
import com.kiosk.server.websocket.message.request.RemoteOrderAcceptRequest;
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
        log.info("대리 주문 요청 처리 시작");
        String userId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");
        RemoteOrderSession session = remoteOrderService.saveSession(userId, familyId, requestMessage.storeId());

        // 키오스크에게 sessionId 전달
        RemoteOrderResponse responseMessage = RemoteOrderResponse.success(
            RemoteOrderResponseType.REQUESTED,
            Map.of(
                "sessionId", session.getSessionId(),
                "username", session.getKioskUserName()
            )
        );

        messagingTemplate.convertAndSend("/topic/" + userId, responseMessage);

        sessionAttributes.put("sessionId", session.getSessionId());

        log.info("대리 주문 요청 처리 완료");
    }

    @MessageMapping("/remote-order/accept")
    public void handleOrderAccept(
        RemoteOrderAcceptRequest message,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        log.info("대리 주문 수락 요청");
        String helperUserId = (String) sessionAttributes.get("userId");
        String familyId = (String) sessionAttributes.get("familyId");

        log.info("수락 처리 시작");
        RemoteOrderSession session = remoteOrderService.acceptSession(message.sessionId(), helperUserId, familyId);
        log.info("수락 처리 완료");

        // Agora 채널 생성 및 전송
        log.info("Agora 채널 생성 시작");
        AgoraChannelInfo agoraChannelInfo = agoraService.createChannel(session.getSessionId());
        log.info("Agora 채널 생성 완료");

        // 양쪽에 Agora 토큰 전달
        RemoteOrderResponse kioskUserResponseMessage = RemoteOrderResponse.success(
            RemoteOrderResponseType.ACCEPTED,
            Map.of(
                "sessionId", session.getSessionId(),
                "helperUserId", helperUserId,
                "kioskUserId", session.getKioskUserId(),
                "partnerName", session.getHelperUserName(),
                "channel", agoraChannelInfo
            )
        );

        RemoteOrderResponse helperUserResponseMessage = RemoteOrderResponse.success(
            RemoteOrderResponseType.ACCEPTED,
            Map.of(
                "sessionId", session.getSessionId(),
                "helperUserId", helperUserId,
                "kioskUserId", session.getKioskUserId(),
                "partnerName", session.getKioskUserName(),
                "channel", agoraChannelInfo
            )
        );

        log.info("수락 완료 메시지 전달 시작");

        messagingTemplate.convertAndSend("/topic/" + helperUserId, helperUserResponseMessage);
        messagingTemplate.convertAndSend("/topic/" + session.getKioskUserId(), kioskUserResponseMessage);

        log.info("수락 완료 메시지 전달 완료");


        sessionAttributes.put("sessionId", session.getSessionId());

        log.info("대리 주문 수락 처리 완료");
    }

}
