package com.kiosk.server.websocket.exception;

import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class WebSocketExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler(RemoteOrderException.class)
    public void handleRemoteOrderException(
        RemoteOrderException exception,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");
        RemoteOrderResponse errorResponse = RemoteOrderResponse.error(exception.getError());
        messagingTemplate.convertAndSend("/topic/" + userId, errorResponse);
    }

    @MessageExceptionHandler(Exception.class)
    public void handleException(
        Exception ex,
        @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
    ) {
        String userId = (String) sessionAttributes.get("userId");
        RemoteOrderResponse errorResponse = RemoteOrderResponse.error(RemoteOrderError.UNKNOWN);
        messagingTemplate.convertAndSend("/topic/" + userId, errorResponse);
        log.error("Unexpected error occurred", ex);
    }
}
