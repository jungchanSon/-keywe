package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.CreateUserProfileService;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserProfileServiceImpl implements CreateUserProfileService {

    private final UserValidateUtil userValidateUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public long doService(long userId, String profileName, String profileRole, String phoneNumber, String profilePass) {

        // null 혹은 빈 값 유효성 검사
        userValidateUtil.validateName(userId, profileName);
        userValidateUtil.validatePhoneNumber(phoneNumber);

        // profileRole로 전환
        ProfileRole role = convertToProfileRole(profileRole);

        UserProfile newProfile;

        if (role == ProfileRole.CHILD) {
            newProfile = UserProfile.createChild(userId, profileName, role, phoneNumber);
        } else if (role == ProfileRole.PARENT) {
            userValidateUtil.validateProfilePass(profilePass);
            newProfile = UserProfile.createParent(userId, profileName, role, phoneNumber, profilePass);
        } else {
            throw new BadRequestException("Invalid profile role");
        }

        userProfileRepository.createNewProfile(newProfile);

        return newProfile.getProfileId();
    }

    private ProfileRole convertToProfileRole(String profileRole) {
        try {
            return ProfileRole.valueOf(profileRole.toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid profile role");
        }
    }

}
