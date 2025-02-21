package com.kiosk.server.domain;

public enum RemoteOrderStatus {
    WAITING,     // 도움 요청 중
    ACCEPTED,    // 수락됨
    TIMEOUT,     // 시간 초과
    ENDED        // 종료됨
}
