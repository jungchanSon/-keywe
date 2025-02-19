package com.kiosk.server.order.application.service.order;

import com.kiosk.server.order.application.port.outbound.UserQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserReader {

    private final UserQueryPort userQueryPort;

    public Long requestUserId(Long profileId) {
        return userQueryPort.findUserIdByProfileId(profileId);
    }
}
