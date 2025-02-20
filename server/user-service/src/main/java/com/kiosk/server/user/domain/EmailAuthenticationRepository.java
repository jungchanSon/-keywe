package com.kiosk.server.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EmailAuthenticationRepository {

    private final RedisTemplate<String, EmailAuthenticationInfo> redisTemplate;
    private static final String EMAIL_AUTHENTICATION_CODE_PREFIX = "email_auth_code:users:";

    public void saveAuthenticationCode(String email, String code) {
        EmailAuthenticationInfo authInfo = new EmailAuthenticationInfo(code, LocalDateTime.now());
        redisTemplate.opsForValue().set(getKey(email), authInfo);
    }

    public String getAuthenticationCode(String email) {
        return getAuthenticationInfo(email).getCode();
    }

    public void deleteAuthenticationCode(String email) {
        redisTemplate.delete(getKey(email));
    }

    public EmailAuthenticationInfo getAuthenticationInfo(String email) {
        return redisTemplate.opsForValue().get(getKey(email));
    }

    public List<String> findAllEmails() {
        Set<String> keys = redisTemplate.keys(EMAIL_AUTHENTICATION_CODE_PREFIX + "*");
        if (keys == null) {
            return Collections.emptyList();
        }
        return keys.stream()
            .map(key -> key.substring(EMAIL_AUTHENTICATION_CODE_PREFIX.length()))
            .collect(Collectors.toList());
    }

    public void deleteAllByEmails(List<String> emails) {
        List<String> keys = emails.stream()
            .map(this::getKey)
            .collect(Collectors.toList());
        redisTemplate.delete(keys);
    }

    private String getKey(String email) {
        return EMAIL_AUTHENTICATION_CODE_PREFIX + email;
    }
}
