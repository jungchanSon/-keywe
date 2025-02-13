package com.kiosk.server.websocket.exception;

public class NotAcceptableRequestStateException extends RemoteOrderException {

    public NotAcceptableRequestStateException() {
        super(RemoteOrderError.NOT_ACCEPTABLE_REQUEST_STATE);
    }
}
