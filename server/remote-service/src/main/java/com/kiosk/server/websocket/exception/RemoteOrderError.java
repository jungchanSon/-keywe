package com.kiosk.server.websocket.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public enum RemoteOrderError {

    UNKNOWN("R000", "알 수 없는 오류 발생"),
    HELPER_NOT_FOUND("R100", "자식을 찾을 수 없습니다."),
    PUSH_NOTIFICATION_SEND_FAILED("R101", "요청 전송에 실패했습니다."),
    CHILD_REMOTE_ORDER_FORBIDDEN("R102", "자식은 주문 요청이 불가합니다."),
    SESSION_NOT_FOUND("R103", "존재하지 않는 세션입니다."),
    UNAUTHORIZED_REMOTE_ORDER_ACCEPT_REQUEST("R104", "수락 권한이 없습니다."),
    UNAUTHORIZED_REMOTE_ORDER_END_REQUEST("R105", "종료 권한이 없습니다."),
    NOT_ACCEPTABLE_REQUEST_STATE("R106", "수락할 수 없는 요청 상태입니다.");

    private final String code;
    private final String message;

    RemoteOrderError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Map<String, Object> toMap() {
        return Map.of("code", code, "message", message);
    }
}
