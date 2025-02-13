package com.kiosk.server.websocket.exception;

public class SessionNotFoundException extends RemoteOrderException {

    public SessionNotFoundException() {
        super(RemoteOrderError.SESSION_NOT_FOUND);
    }
}
