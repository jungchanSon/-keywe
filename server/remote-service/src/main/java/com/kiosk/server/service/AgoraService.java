package com.kiosk.server.service;

import com.kiosk.server.service.dto.AgoraChannelInfo;
import io.agora.media.RtcTokenBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AgoraService {

    @Value("${agora.app-id}")
    private String agoraAppId;

    @Value("${agora.app-certificate}")
    private String agoraAppCertificate;

    public AgoraChannelInfo createChannel(String channelName) {
        RtcTokenBuilder token = new RtcTokenBuilder();
        String rtcToken = token.buildTokenWithUid(
            agoraAppId,
            agoraAppCertificate,
            channelName,
            0,
            RtcTokenBuilder.Role.Role_Publisher,
            (int) (System.currentTimeMillis() / 1000) + 3600
        );

        return new AgoraChannelInfo(channelName, rtcToken);
    }
}
