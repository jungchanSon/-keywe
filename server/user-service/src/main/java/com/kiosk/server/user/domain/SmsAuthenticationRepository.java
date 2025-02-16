package com.kiosk.server.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class SmsAuthenticationRepository {

    private final RedisTemplate<String, Object> objectRedisTemplate;
    private static final String SMS_AUTHENTICATION_CODE_PREFIX = "sms_auth_code:users:";
    private static final String SMS_RATE_LIMIT_PREFIX = "sms_rate_limit:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5); // 5분 유효기간
    private static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(3); // 3분 제한
    private static final int MAX_REQUESTS = 3;

    // 전화번호에 대한 SMS 인증 코드를 Redis에 저장
    public void saveAuthenticationCode(String phone, String code) {
        String key = SMS_AUTHENTICATION_CODE_PREFIX + phone;
        objectRedisTemplate.opsForValue().set(key, code, CODE_EXPIRATION);
    }

    // 전화번호에 해당하는 SMS 인증 코드를 조회 (없으면 null)
    public String getAuthenticationCode(String phone) {
        String key = SMS_AUTHENTICATION_CODE_PREFIX + phone;
        Object code = objectRedisTemplate.opsForValue().get(key);
        return code != null ? code.toString() : null;
    }

    // 전화번호에 해당하는 SMS 인증 코드를 삭제
    public void deleteAuthenticationCode(String phone) {
        String key = SMS_AUTHENTICATION_CODE_PREFIX + phone;
        objectRedisTemplate.delete(key);
    }

    public boolean checkRateLimit(String phone) {
        String key = SMS_RATE_LIMIT_PREFIX + phone;
        Long currentCount = objectRedisTemplate.opsForValue().increment(key);

        if (currentCount == 1) {
            objectRedisTemplate.expire(key, RATE_LIMIT_WINDOW);
        }

        return currentCount <= MAX_REQUESTS;
    }
}
