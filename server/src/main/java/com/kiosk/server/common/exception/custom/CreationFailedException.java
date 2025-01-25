package com.kiosk.server.common.exception.custom;

public class CreationFailedException extends RuntimeException {
    public CreationFailedException(String message) {
        super(message);
    }
}
