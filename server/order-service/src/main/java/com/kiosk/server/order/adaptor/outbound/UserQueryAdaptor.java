package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.infra.feign.client.UserClient;
import com.kiosk.server.order.application.port.outbound.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryAdaptor implements UserQueryPort {

    private final UserClient userClient;

    @Override
    public Long findUserIdByProfileId(Long profileId) {
        return userClient.getUserId(profileId).orElseThrow(
                () -> new RuntimeException("[order][주문]회원 조회 실패. profileId="+profileId)
        );
    }
}
