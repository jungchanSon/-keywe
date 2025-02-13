package com.kiosk.server.websocket.exception;

public class PushNotificationSendFailedException extends RemoteOrderException {

    public PushNotificationSendFailedException() {
        super(RemoteOrderError.PUSH_NOTIFICATION_SEND_FAILED);
    }
}
