package com.kiosk.server.user.util;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.ConflictException;
import com.kiosk.server.user.domain.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidateUtil {

    private final UserProfileRepository userProfileRepository;

    // 이름 중복 여부
    public void validateName(long userId, String profileName) {

        if (userId <= 0 || profileName == null || profileName.isBlank()) {
            throw new IllegalArgumentException("유효한 사용자 정보와 프로필 이름을 입력해 주세요.");
        }

        int isDuplicatedName = userProfileRepository.checkDuplicateProfileName(userId, profileName);

        if (isDuplicatedName > 0) {
            throw new ConflictException("이미 사용 중인 프로필 이름입니다. 다른 이름을 입력해 주세요.");
        }
    }

    // 휴대폰 번호 검증
    public void validatePhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new BadRequestException("휴대폰 번호를 입력해 주세요.");
        }

        String phoneRegex = "^01[016789]\\d{7,8}$";

        // 휴대폰번호 검증
        if (phoneNumber.length() != 11 && !phoneNumber.matches(phoneRegex)) {
            throw new BadRequestException("유효한 휴대폰 번호 형식이 아닙니다. 올바른 번호를 입력해 주세요.");
        }
    }

    // 인증 비밀번호 검증
    public void validateProfilePass(String profilePass) {
        if (profilePass == null || !profilePass.matches("^\\d{4}$")) {
            throw new BadRequestException("비밀번호는 4자리 숫자로 입력해 주세요.");
        }
    }


}
