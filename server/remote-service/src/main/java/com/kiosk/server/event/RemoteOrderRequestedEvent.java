package com.kiosk.server.event;

import com.kiosk.server.domain.RemoteOrderSession;

public record RemoteOrderRequestedEvent(RemoteOrderSession session) {
}
