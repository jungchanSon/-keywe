package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.user.controller.dto.CreateProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.CreateUserProfileService;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserProfileServiceImpl implements CreateUserProfileService {

    private final UserValidateUtil userValidateUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public CreateProfileResponse doService(long userId, String profileName, String profileRole, String phoneNumber, String profilePass) {
        log.info("CreateUserProfileService: userId={}, profileName={}, profileRole={}",
                userId, profileName, profileRole);

        // null 혹은 빈 값 유효성 검사
        userValidateUtil.validateName(userId, profileName);

        // profileRole로 전환
        ProfileRole role = convertToProfileRole(profileRole);

        UserProfile newProfile;

        if (role == ProfileRole.CHILD) {
            newProfile = UserProfile.createChild(userId, profileName, role, phoneNumber);
        } else if (role == ProfileRole.PARENT) {
            userValidateUtil.validatePhoneNumber(phoneNumber);
            userValidateUtil.validateProfilePass(profilePass);
            newProfile = UserProfile.createParent(userId, profileName, role, phoneNumber, profilePass);
        } else {
            log.warn("잘못된 프로필 역할 지정. userId={}, profileRole={}", userId, profileRole);
            throw new BadRequestException("프로필 역할이 올바르지 않습니다. 올바른 역할을 선택해 주세요.");
        }

        userProfileRepository.createNewProfile(newProfile);

        log.info("프로필 생성 완료. userId={}, profileId={}, profileRole={}",
                userId, newProfile.getProfileId(), newProfile.getProfileRole());

        LocalDateTime regDate = newProfile.getRegDate();
        String CratedAt = regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return new CreateProfileResponse(
                String.valueOf(newProfile.getProfileId()),
                newProfile.getProfileRole(),
                CratedAt
        );
    }

    private ProfileRole convertToProfileRole(String profileRole) {
        try {
            return ProfileRole.valueOf(profileRole.toUpperCase());
        } catch (Exception e) {
            log.warn("프로필 역할 변환 실패. 입력값={}", profileRole);
            throw new BadRequestException("프로필 역할이 올바르지 않습니다. 올바른 역할을 선택해 주세요.");
        }
    }

}
