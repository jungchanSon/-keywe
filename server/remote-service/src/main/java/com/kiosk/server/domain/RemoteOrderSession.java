package com.kiosk.server.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RemoteOrderSession {

    private String sessionId;
    private String familyId;
    private String kioskUserId;
    private String kioskUserName;
    private String helperUserId;
    private String helperUserName;
    private String storeId;
    private String status;
    private String createdAt;

    @Builder
    public RemoteOrderSession(String sessionId, String familyId, String kioskUserId, String kioskUserName, String helperUserId, String helperUserName, String storeId, String status, String createdAt) {
        this.sessionId = sessionId;
        this.familyId = familyId;
        this.kioskUserId = kioskUserId;
        this.kioskUserName = kioskUserName;
        this.helperUserId = helperUserId;
        this.helperUserName = helperUserName;
        this.storeId = storeId;
        this.status = status;
        this.createdAt = createdAt;
    }
}
