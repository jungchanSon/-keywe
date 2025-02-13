package com.kiosk.server.event;

import com.kiosk.server.domain.PushToken;

import java.util.List;

public record InvalidPushTokensDetectedEvent(List<PushToken> invalidTokens) {
}
