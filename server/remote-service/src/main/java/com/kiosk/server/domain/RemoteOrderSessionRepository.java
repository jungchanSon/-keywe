package com.kiosk.server.domain;

import com.kiosk.server.client.feign.dto.UserProfile;
import com.kiosk.server.websocket.exception.NotAcceptableRequestStateException;
import com.kiosk.server.websocket.exception.SessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RemoteOrderSessionRepository {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, RemoteOrderSession> sessionRedisTemplate;

    private static final String HELPER_PREFIX = "remote_order:helpers:";
    private static final String SESSION_PREFIX = "remote_order:session:";
    private static final Duration SESSION_TTL = Duration.ofDays(1);

    public RemoteOrderSession findById(String sessionId) {
        return sessionRedisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
    }

    public void save(RemoteOrderSession session) {
        sessionRedisTemplate.opsForValue().set(
            SESSION_PREFIX + session.getSessionId(),
            session
        );
    }

    public void saveWithTTL(RemoteOrderSession session) {
        sessionRedisTemplate.opsForValue().set(
            SESSION_PREFIX + session.getSessionId(),
            session,
            SESSION_TTL
        );
    }

    public Map<String, String> findHelperIdNameMap(String familyId) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash()
            .entries(HELPER_PREFIX + familyId);

        return entries.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> entry.getValue().toString()
            ));
    }

    public void saveHelperIdNameMap(String familyId, List<UserProfile> helperProfiles) {
        String key = HELPER_PREFIX + familyId;
        Map<String, String> helperMap = helperProfiles.stream()
            .collect(Collectors.toMap(
                UserProfile::id,
                UserProfile::name
            ));

        stringRedisTemplate.opsForHash().putAll(key, helperMap);
    }

    public boolean isHelper(String familyId, String userId) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForHash().hasKey(HELPER_PREFIX + familyId, userId));
    }

    public String getHelperName(String familyId, String userId) {
        return (String) stringRedisTemplate.opsForHash().get(HELPER_PREFIX + familyId, userId);
    }

    public RemoteOrderSession acceptSession(String sessionId, String helperUserId) {
        return sessionRedisTemplate.execute(new SessionCallback<>() {
            @Override
            public RemoteOrderSession execute(RedisOperations operations) throws DataAccessException {
                String key = SESSION_PREFIX + sessionId;

                operations.watch(key);

                RemoteOrderSession session = (RemoteOrderSession) operations.opsForValue().get(key);
                if (session == null) {
                    throw new SessionNotFoundException();
                }
                if (!RemoteOrderStatus.WAITING.name().equals(session.getStatus())) {
                    operations.unwatch();
                    throw new NotAcceptableRequestStateException();
                }

                operations.multi();

                session.setHelperUserId(helperUserId);
                session.setStatus(RemoteOrderStatus.ACCEPTED.name());
                operations.opsForValue().set(key, session);

                if (CollectionUtils.isEmpty(operations.exec())) {
                    throw new NotAcceptableRequestStateException();
                }
                return session;
            }
        });
    }
}
