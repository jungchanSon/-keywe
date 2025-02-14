package com.kiosk.server.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class EmailAuthenticationRepository {

    private final RedisTemplate<String, Object> objectRedisTemplate;
    private static final String EMAIL_AUTHENTICATION_CODE_PREFIX = "email_auth_code:users:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(30); // 30분 유효기간

    public void saveAuthenticationCode(String email, String code) {
        String key = EMAIL_AUTHENTICATION_CODE_PREFIX + email;
        objectRedisTemplate.opsForValue().set(key, code, CODE_EXPIRATION);
    }

    public String getAuthenticationCode(String email) {
        String key = EMAIL_AUTHENTICATION_CODE_PREFIX + email;
        Object code = objectRedisTemplate.opsForValue().get(key);
        return code != null ? code.toString() : null;
    }

    public void deleteAuthenticationCode(String email) {
        String key = EMAIL_AUTHENTICATION_CODE_PREFIX + email;
        objectRedisTemplate.delete(key);
    }
}
