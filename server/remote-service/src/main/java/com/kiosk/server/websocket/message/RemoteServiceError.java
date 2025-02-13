package com.kiosk.server.websocket.message;

import lombok.Getter;

import java.util.Map;

@Getter
public enum RemoteServiceError {

    SESSION_TIMEOUT("R100", "만료된 주문 요청입니다."),
    PUSH_NOTIFICATION_SEND_FAILED("R101", "가족에게 요청을 보내지 못했어요."),
    CHILD_REMOTE_ORDER_FORBIDDEN("R102", "자식 프로필은 주문 요청이 불가합니다.");

    private final String code;
    private final String message;

    RemoteServiceError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Map<String, Object> toMap() {
        return Map.of("code", code, "message", message);
    }
}
