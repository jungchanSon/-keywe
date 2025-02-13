package com.kiosk.server.websocket.message.response;

public enum RemoteOrderResponseType {
    REQUESTED,   // 키오스크의 요청에 대한 응답 (sessionId 전달)
    WAITING,    // 헬퍼 수락 대기 (알림 전송 정보 포함)
    ACCEPTED,    // 헬퍼의 수락 (Agora 정보 포함)
    TIMEOUT,     // 시간 초과
    END,         // 종료
    ERROR       // 에러
}
