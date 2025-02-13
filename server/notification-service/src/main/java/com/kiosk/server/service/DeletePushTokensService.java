package com.kiosk.server.service;

import com.kiosk.server.domain.PushToken;

import java.util.List;

public interface DeletePushTokensService {

    void doService(List<PushToken> pushTokens);
}
