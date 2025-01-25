package com.kiosk.server.user.domain;

import com.kiosk.server.common.exception.custom.UnauthorizedException;

public enum UserRole {
    CUSTOMER, CEO, MANAGER;

    // String -> UserRole 변환
    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (final UnauthorizedException e) {
            throw new UnauthorizedException("Invalid user role: " + role);
        }
    }


    // UserRole -> String
    public String toString() {
        return this.name();
    }

}
