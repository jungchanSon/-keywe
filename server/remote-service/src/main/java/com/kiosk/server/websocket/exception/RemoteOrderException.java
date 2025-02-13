package com.kiosk.server.websocket.exception;

import lombok.Getter;

@Getter
public class RemoteOrderException extends RuntimeException {

    private final RemoteOrderError error;

    public RemoteOrderException(RemoteOrderError error) {
        super(error.getMessage());
        this.error = error;
    }
}
