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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new UnauthorizedException("Incorrect Credentials");
        }

        // 비밀번호 검증
        String hashedInputPassword = HashUtil.hashPassword(password, foundUser.getSalt());

        if (!foundUser.getPassword().equals(hashedInputPassword)) {
            throw new UnauthorizedException("Incorrect Credentials");
        }

        String accessToken = tokenUtil.createAuthenticationToken(foundUser.getUserId(), foundUser.getUserId());
        return new CeoLoginResult(accessToken, String.valueOf(foundUser.getUserId()));
    }
}
