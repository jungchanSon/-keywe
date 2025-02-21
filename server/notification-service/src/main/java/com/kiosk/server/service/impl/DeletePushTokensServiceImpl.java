package com.kiosk.server.service.impl;

import com.kiosk.server.domain.PushToken;
import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.service.DeletePushTokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletePushTokensServiceImpl implements DeletePushTokensService {

    private final PushTokenRepository pushTokenRepository;

    @Override
    @Transactional
    public void doService(List<PushToken> pushTokens) {
        if (pushTokens == null || pushTokens.isEmpty()) {
            return;
        }
        pushTokenRepository.deletePushTokensInBatch(pushTokens);
    }
}
