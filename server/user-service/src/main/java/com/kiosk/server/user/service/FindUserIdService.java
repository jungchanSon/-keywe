package com.kiosk.server.user.service;

import java.util.Optional;

public interface FindUserIdService {
    Optional<Long> doService(Long profileId);
}
