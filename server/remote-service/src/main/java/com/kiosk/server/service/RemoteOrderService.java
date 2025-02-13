package com.kiosk.server.service;

import com.kiosk.server.client.feign.api.UserClient;
import com.kiosk.server.client.feign.dto.UserProfile;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.domain.RemoteOrderStatus;
import com.kiosk.server.event.RemoteOrderRequestedEvent;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import com.kiosk.server.websocket.message.response.RemoteOrderError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteOrderService {

    private final UserClient userClient;
    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedisTemplate<String, RemoteOrderSession> sessionRedisTemplate;

    private static final String HELPER_PREFIX = "remote_order:helpers:";
    private static final String SESSION_PREFIX = "remote_order:session:";
    private static final Duration SESSION_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration SESSION_TTL = Duration.ofDays(1);

    // Redis에 세션 저장
    public String createSession(String userId, String familyId, String storeId) {

        if (!userClient.verifyParentRole(userId)) {
            return null;
        }

        RemoteOrderSession session = RemoteOrderSession.builder()
            .sessionId(String.valueOf(IdUtil.create()))
            .familyId(familyId)
            .kioskUserId(userId)
            .storeId(storeId)
            .status(RemoteOrderStatus.WAITING.name())
            .createdAt(LocalDateTime.now().toString())
            .build();

        sessionRedisTemplate.opsForValue().set(SESSION_PREFIX + session.getSessionId(), session);

        // 가족 구성원 식별자 저장 (추후 요청 수락시 인가 처리용)
        List<String> availableHelperUserIds = userClient.getHelperProfiles(familyId)
            .stream()
            .map(UserProfile::id)
            .toList();

        objectRedisTemplate.opsForSet().add(HELPER_PREFIX + familyId, availableHelperUserIds.toArray());

        // 타임아웃 체크 스케줄링
        scheduleTimeoutCheck(session.getSessionId());

        eventPublisher.publishEvent(new RemoteOrderRequestedEvent(session));

        return session.getSessionId();
    }

    public RemoteOrderSession acceptSession(String sessionId, String helperUserId, String familyId) {
        // 가족 구성원이 맞는지 확인
        Boolean isMember = objectRedisTemplate.opsForSet().isMember(HELPER_PREFIX + familyId, helperUserId);
        if (Boolean.FALSE.equals(isMember)) {
            return null;
        }

        String key = SESSION_PREFIX + sessionId;

        // 세션 가져오기 및 상태 검증을 원자적 작업으로 수행
        return sessionRedisTemplate.execute(new SessionCallback<>() {
            @Override
            public RemoteOrderSession execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);  // key 감시 시작

                RemoteOrderSession session = (RemoteOrderSession) operations.opsForValue().get(key);

                // 유효성 검증
                if (session == null || !RemoteOrderStatus.WAITING.name().equals(session.getStatus())) {
                    operations.unwatch();  // 감시 해제
                    return null;
                }

                // 트랜잭션 시작
                operations.multi();

                // 세션 업데이트
                session.setHelperUserId(helperUserId);
                session.setStatus(RemoteOrderStatus.ACCEPTED.name());
                operations.opsForValue().set(key, session);

                // 트랜잭션 실행
                return !CollectionUtils.isEmpty(operations.exec()) ? session : null;
            }
        });
    }

    public RemoteOrderSession endSession(String userId, String sessionId) {
        String key = SESSION_PREFIX + sessionId;

        RemoteOrderSession session = sessionRedisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        if (session == null) {
            return null;
        }

        // 요청자나 수락자만 종료 가능
        if (!userId.equals(session.getKioskUserId()) && !userId.equals(session.getHelperUserId())) {
            return null;
        }

        session.setStatus(RemoteOrderStatus.ENDED.name());
        sessionRedisTemplate.opsForValue().set(key, session, SESSION_TTL);

        return session;
    }

    // 타임아웃 체크 스케줄링
    private void scheduleTimeoutCheck(String sessionId) {
        taskScheduler.schedule(
            () -> checkTimeout(sessionId),
            Instant.now().plusSeconds(SESSION_TIMEOUT.toSeconds())
        );
    }

    // 타임아웃 체크 및 처리
    private void checkTimeout(String sessionId) {
        RemoteOrderSession session = sessionRedisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        String key = SESSION_PREFIX + sessionId;

        if (session != null && RemoteOrderStatus.WAITING.name().equals(session.getStatus())) {
            session.setStatus(RemoteOrderStatus.TIMEOUT.name());
            sessionRedisTemplate.opsForValue().set(key, session, SESSION_TTL);

            messagingTemplate.convertAndSend(
                "/topic/" + session.getKioskUserId(),
                RemoteOrderResponse.error(RemoteOrderError.SESSION_TIMEOUT)
            );
        }
    }
}
