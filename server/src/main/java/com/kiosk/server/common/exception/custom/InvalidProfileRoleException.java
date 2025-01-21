package com.kiosk.server.common.exception.custom;

public class InvalidProfileRoleException extends RuntimeException {
    public InvalidProfileRoleException(String message) {
        super(message);
    }
}
