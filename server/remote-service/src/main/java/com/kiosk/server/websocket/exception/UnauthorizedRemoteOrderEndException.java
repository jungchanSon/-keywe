package com.kiosk.server.websocket.exception;

public class UnauthorizedRemoteOrderEndException extends RemoteOrderException {

    public UnauthorizedRemoteOrderEndException() {
        super(RemoteOrderError.UNAUTHORIZED_REMOTE_ORDER_END_REQUEST);
    }
}
