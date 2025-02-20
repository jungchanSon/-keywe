package com.kiosk.server.order.application.port.outbound;

public interface UserQueryPort {
    Long findUserIdByProfileId(Long profileId);
}
