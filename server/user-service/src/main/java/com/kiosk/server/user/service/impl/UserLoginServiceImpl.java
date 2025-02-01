package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.common.util.HashUtil;
import com.kiosk.server.common.util.TokenUtil;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    @Override
    public String doService(String email, String inputPassword) {

        User foundUser = userRepository.findByEmail(email);

        if(foundUser == null) {
            throw new UnauthorizedException("Incorrect Credentials");
        }

        // 비밀번호 검증
        String hashedInputPassword = HashUtil.hashPassword(inputPassword, foundUser.getSalt());

        if (!foundUser.getPassword().equals(hashedInputPassword)) {
            throw new UnauthorizedException("Incorrect Credentials");
        }

        return tokenUtil.createTemporaryToken(foundUser.getUserId());

    }
}
