package com.kiosk.server.event;

import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.domain.RemoteOrderSessionRepository;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import com.kiosk.server.websocket.message.response.RemoteOrderResponseType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class SessionDisconnectEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RemoteOrderSessionRepository remoteOrderSessionRepository;

    @EventListener(SessionDisconnectEvent.class)
    public void onSessionDisconnected(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = (String) accessor.getSessionAttributes().get("sessionId");
        String userId = (String) accessor.getSessionAttributes().get("userId");

        if (sessionId != null && userId != null) {
            RemoteOrderSession session = remoteOrderSessionRepository.findById(sessionId);
            if (session != null) {
                String targetUserId = userId.equals(session.getKioskUserId())
                    ? session.getHelperUserId()
                    : session.getKioskUserId();

                messagingTemplate.convertAndSend("/topic/" + targetUserId,
                    RemoteOrderResponse.success(RemoteOrderResponseType.END));
            }
        }
    }
}
