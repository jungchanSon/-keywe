package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.controller.dto.LoginResponse;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.domain.UserRole;
import com.kiosk.server.user.service.UserLoginService;
import com.kiosk.server.user.util.HashUtil;
import com.kiosk.server.user.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    @Override
    public LoginResponse doService(String email, String inputPassword) {
        log.info("UserLoginService: email={}", email);

        User foundUser = userRepository.findByEmail(email);

        if(foundUser == null) {
            log.warn("로그인 실패 - 존재하지 않는 이메일 입력: email={}", email);
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 이메일을 입력해 주세요.");
        }

        // 비밀번호 검증
        String hashedInputPassword = HashUtil.hashPassword(inputPassword, foundUser.getSalt());

        if (!foundUser.getPassword().equals(hashedInputPassword)) {
            log.warn("로그인 실패 - 잘못된 비밀번호 입력: email={}", email);
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 비밀번호를 입력해 주세요.");
        }

        log.info("로그인 성공 - userId={}", foundUser.getUserId());

        String temporaryToken = tokenUtil.createTemporaryToken(foundUser.getUserId());

        UserRole role = foundUser.getRole();

        return new LoginResponse(temporaryToken, role);
    }
}
