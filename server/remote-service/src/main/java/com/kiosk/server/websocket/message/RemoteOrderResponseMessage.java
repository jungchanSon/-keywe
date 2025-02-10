package com.kiosk.server.websocket.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RemoteOrderResponseMessage(
    RemoteOrderResponseMessageType type,
    String sessionId,
    AgoraChannelInfo channelInfo,
    String targetId,
    Map<String, Object> data,
    String timestamp
) {
}
