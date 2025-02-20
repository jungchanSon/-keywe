package com.kiosk.server.websocket.exception;

public class HelperNotFoundException extends RemoteOrderException {

    public HelperNotFoundException() {
        super(RemoteOrderError.HELPER_NOT_FOUND);
    }
}
