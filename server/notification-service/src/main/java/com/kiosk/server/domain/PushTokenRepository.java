package com.kiosk.server.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends Repository<PushToken, Integer> {

    void save(PushToken pushToken);

    void delete(PushToken token);

    Optional<PushToken> findByDeviceId(String deviceId);

    List<PushToken> findByUserId(Long userId);

    List<PushToken> findByProfileId(Long profileId);

    @Modifying
    @Query("DELETE FROM PushToken pt WHERE pt IN :pushTokens")
    void deletePushTokensInBatch(List<PushToken> pushTokens);
}
