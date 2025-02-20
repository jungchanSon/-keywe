package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.domain.EmailAuthenticationInfo;
import com.kiosk.server.user.domain.EmailAuthenticationRepository;
import com.kiosk.server.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanUpAuthenticationExpiredUsersService {

    private final UserRepository userRepository;
    private final EmailAuthenticationRepository emailAuthRepository;

    @Transactional
    public void execute() {
        List<String> emails = emailAuthRepository.findAllEmails();

        List<String> expiredEmails = emails.stream()
            .filter(email -> {
                EmailAuthenticationInfo authInfo = emailAuthRepository.getAuthenticationInfo(email);
                return authInfo != null && authInfo.checkExpired();
            })
            .collect(Collectors.toList());

        if (!expiredEmails.isEmpty()) {
            log.info("Starting cleanup of expired users");

            // users 테이블에서 미인증 데이터 삭제
            userRepository.deleteUnverifiedUsersByEmails(expiredEmails);

            // redis에서 인증 정보 삭제
            emailAuthRepository.deleteAllByEmails(expiredEmails);

            log.info("Cleaned up {} expired users", expiredEmails.size());
        }
    }
}
