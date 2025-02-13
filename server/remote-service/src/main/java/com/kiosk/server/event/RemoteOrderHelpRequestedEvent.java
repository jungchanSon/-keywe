package com.kiosk.server.event;

import com.kiosk.server.domain.RemoteOrderSession;

public record RemoteOrderHelpRequestedEvent(RemoteOrderSession session) {
}
