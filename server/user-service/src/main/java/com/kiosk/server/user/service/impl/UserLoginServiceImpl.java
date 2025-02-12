package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.service.UserLoginService;
import com.kiosk.server.user.util.HashUtil;
import com.kiosk.server.user.util.TokenUtil;
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
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 이메일을 입력해 주세요.");
        }

        // 비밀번호 검증
        String hashedInputPassword = HashUtil.hashPassword(inputPassword, foundUser.getSalt());

        if (!foundUser.getPassword().equals(hashedInputPassword)) {
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 비밀번호를 입력해 주세요.");
        }

        return tokenUtil.createTemporaryToken(foundUser.getUserId());

    }
}
