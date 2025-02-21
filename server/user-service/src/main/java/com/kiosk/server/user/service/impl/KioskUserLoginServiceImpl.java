package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.controller.dto.KioskUserLoginResult;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.KioskUserLoginService;
import com.kiosk.server.user.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class KioskUserLoginServiceImpl implements KioskUserLoginService {

    private final TokenUtil tokenUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public KioskUserLoginResult doService(String phone, String password) {
        log.info("KioskUserLoginService: phone={}", phone);

        validatePhoneNumber(phone);
        validatePassword(password);

        UserProfile profile = userProfileRepository.findByPhoneAndPassword(phone, password);
        if (profile == null) {
            log.warn("로그인 실패 - phone={}", phone);
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 정보로 다시 시도해 주세요.");
        }
        String accessToken = tokenUtil.createAuthenticationToken(profile.getUserId(), profile.getProfileId());
        log.info("로그인 성공 - userId={}, profileId={}", profile.getUserId(), profile.getProfileId());

        return new KioskUserLoginResult(accessToken, String.valueOf(profile.getProfileId()));
    }

    private void validatePhoneNumber(String phone) {
        String regex = "^01(?:0|1|[6-9])-?(?:\\d{3}|\\d{4})-?\\d{4}$";
        if (!Pattern.matches(regex, phone)) {
            log.warn("유효하지 않은 휴대폰 번호 입력 - phone={}", phone);
            throw new BadRequestException("휴대폰 번호 형식이 올바르지 않습니다.");
        }
    }

    private void validatePassword(String password) {
        String regex = "^\\d{4}$";
        if (!Pattern.matches(regex, password)) {
            log.warn("유효하지 않은 비밀번호 입력");
            throw new BadRequestException("비밀번호는 숫자 4자리여야 합니다.");
        }
    }
}
