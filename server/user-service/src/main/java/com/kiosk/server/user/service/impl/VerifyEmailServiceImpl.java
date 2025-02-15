package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.user.domain.EmailAuthenticationRepository;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.service.VerifyEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEmailServiceImpl implements VerifyEmailService {

    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void doService(String email, String token) {
        String savedToken = emailAuthenticationRepository.getAuthenticationCode(email);

        if (savedToken == null) {
            throw new BadRequestException("만료되었거나 존재하지 않는 인증 링크입니다.");
        }

        if (!savedToken.equals(token)) {
            throw new BadRequestException("유효하지 않은 인증 링크입니다.");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("사용자를 찾을 수 없습니다.");
        }

        userRepository.verifyEmail(user.getUserId());

        emailAuthenticationRepository.deleteAuthenticationCode(email);

        log.info("이메일 인증 완료 - email={}", email);
    }
}
