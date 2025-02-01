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
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        int isDuplicatedName = userProfileRepository.checkDuplicateProfileName(userId, profileName);

        if (isDuplicatedName > 0) {
            throw new ConflictException("Name Duplicated");
        }
    }

    // 휴대폰 번호 검증
    public void validatePhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("PhoneNumber cannot be null or empty.");
        }

        String phoneRegex = "^01[016789]\\d{7,8}$";

        // 휴대폰번호 검증
        if (phoneNumber.length() != 11 && !phoneNumber.matches(phoneRegex)) {
            throw new BadRequestException("Invalid PhoneNumber Format");
        }
    }

    // 인증 비밀번호 검증
    public void validateProfilePass(String profilePass) {
        if (profilePass == null || !profilePass.matches("^\\d{4}$")) {
            throw new BadRequestException("Invalid password type");
        }
    }


}
