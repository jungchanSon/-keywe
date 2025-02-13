package com.kiosk.server.websocket.exception;

public class ChildRemoteOrderForbiddenException extends RemoteOrderException {

    public ChildRemoteOrderForbiddenException() {
        super(RemoteOrderError.CHILD_REMOTE_ORDER_FORBIDDEN);
    }
}
