package com.kiosk.server.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushToken, Integer> {

    Optional<PushToken> findByDeviceId(String deviceId);

    List<PushToken> findByUserId(Long userId);

    @Query("SELECT pt FROM PushToken pt WHERE pt.userId IN :userIds")
    List<PushToken> findByUserIds(@Param("userIds") List<Long> userIds);


    @Query("SELECT pt FROM PushToken pt WHERE pt.profileId IN :profileIds")
    List<PushToken> findByProfileIds(@Param("profileIds") List<Long> profileIds);

    List<PushToken> findByProfileId(Long profileId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM PushToken pt WHERE pt IN :pushTokens")
    void deletePushTokensInBatch(@Param("pushTokens") List<PushToken> pushTokens);
}
