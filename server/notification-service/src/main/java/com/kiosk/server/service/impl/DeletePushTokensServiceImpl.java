package com.kiosk.server.service.impl;

import com.kiosk.server.domain.PushToken;
import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.service.DeletePushTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletePushTokensServiceImpl implements DeletePushTokensService {

    private final PushTokenRepository pushTokenRepository;

    @Override
    public void doService(List<PushToken> pushTokens) {
        pushTokenRepository.deletePushTokensInBatch(pushTokens);
    }
}
