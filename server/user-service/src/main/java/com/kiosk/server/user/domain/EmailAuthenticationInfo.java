package com.kiosk.server.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuthenticationInfo implements Serializable {

    private String code;
    private LocalDateTime registeredAt;

    public boolean checkExpired() {
        return registeredAt.plusMinutes(60).isBefore(LocalDateTime.now());
    }
}
