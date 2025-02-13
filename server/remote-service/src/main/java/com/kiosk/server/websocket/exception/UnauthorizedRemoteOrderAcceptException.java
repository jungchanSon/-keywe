package com.kiosk.server.websocket.exception;

public class UnauthorizedRemoteOrderAcceptException extends RemoteOrderException {

    public UnauthorizedRemoteOrderAcceptException() {
        super(RemoteOrderError.UNAUTHORIZED_REMOTE_ORDER_ACCEPT_REQUEST);
    }
}
