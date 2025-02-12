package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.user.controller.dto.PatchProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.ModifyUserProfileService;
import com.kiosk.server.user.util.UserProfileUtil;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ModifyUserProfileServiceImpl implements ModifyUserProfileService {

    private final UserProfileUtil userProfileUtil;
    private final UserValidateUtil userValidateUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public PatchProfileResponse doService(long userId, long profileId, String profileName, String phoneNumber, String profilePass) {

        // userProfile 조회
        UserProfile userProfile = fetchUserProfile(userId, profileId);

        ProfileRole role = userProfile.getProfileRole();

        String updatedName = profileName != null ? profileName : userProfile.getProfileName();
        String updatedPhone = phoneNumber != null ? phoneNumber : userProfile.getPhoneNumber();

        // Role에 따라 필요한 필드를  처리
        Map<String, Object> updateProfileParam = userProfileUtil.createProfileParams(userId, profileId, updatedName, updatedPhone);

        // 역할에 따른 처리
        if (role.equals(ProfileRole.PARENT)) {
            userValidateUtil.validatePhoneNumber(updatedPhone);
            userValidateUtil.validateProfilePass(profilePass);
            updateProfileParam.put("profilePass", profilePass);
            userProfileRepository.updateParentProfile(updateProfileParam);

        } else if (role.equals(ProfileRole.CHILD)) {
            updateProfileParam.remove("phoneNumber", updatedPhone);
            userProfileRepository.updateChildProfile(updateProfileParam);

        } else {
            throw new BadRequestException("프로필 역할이 선택되지 않았습니다. 올바른 프로필 역할을 선택해주세요.");
        }

        UserProfile user = userProfileUtil.getUserProfileById(userId, profileId);

        return new PatchProfileResponse(
                user.getProfileRole(),
                user.getProfileName(),
                user.getPhoneNumber(),
                user.getProfilePass()
        );
    }

    private UserProfile fetchUserProfile(long userId, long profileId) {
        UserProfile userProfile = userProfileUtil.getUserProfileById(userId, profileId);
        if (userProfile == null) {
            throw new EntityNotFoundException("수정할 프로필이 선택되지 않았습니다. 수정할 프로필을 선택한 후 다시 시도해 주세요.");
        }
        return userProfile;
    }

}
