package com.kiosk.server.websocket.message;

public enum RemoteOrderResponseMessageType {
    REQUESTED,   // 키오스크의 요청에 대한 응답 (sessionId 전달)
    ACCEPTED,    // 헬퍼의 수락 (Agora 정보 포함)
    TIMEOUT,     // 시간 초과
    END,         // 종료
    ERROR
}
