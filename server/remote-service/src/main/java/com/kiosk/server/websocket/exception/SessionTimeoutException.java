package com.kiosk.server.websocket.exception;

public class SessionTimeoutException extends RemoteOrderException {

    public SessionTimeoutException() {
        super(RemoteOrderError.SESSION_TIMEOUT);
    }
}
