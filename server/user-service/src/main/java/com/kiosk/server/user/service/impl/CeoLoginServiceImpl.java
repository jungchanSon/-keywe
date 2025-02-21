package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.controller.dto.CeoLoginResult;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.domain.UserRole;
import com.kiosk.server.user.service.CeoLoginService;
import com.kiosk.server.user.util.HashUtil;
import com.kiosk.server.user.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CeoLoginServiceImpl implements CeoLoginService {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CeoLoginResult doService(String email, String password) {
        User foundUser = userRepository.findByEmail(email);

        if (foundUser == null || foundUser.getRole() != UserRole.CEO) {
            log.info("사장님 로그인 실패 - 존재하지 않는 이메일: email={}", email);
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }

        // 비밀번호 검증
        String hashedInputPassword = HashUtil.hashPassword(password, foundUser.getSalt());

        if (!foundUser.getPassword().equals(hashedInputPassword)) {
            log.info("사장님 로그인 실패 - 잘못된 비밀번호 입력: email={}", email);
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }

        log.info("사장님 로그인 성공 - userId={}", foundUser.getUserId());
        String accessToken = tokenUtil.createAuthenticationToken(foundUser.getUserId(), foundUser.getUserId());
        return new CeoLoginResult(accessToken, String.valueOf(foundUser.getUserId()));
    }
}
