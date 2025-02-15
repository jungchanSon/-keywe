package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.image.service.DeleteImageService;
import com.kiosk.server.image.service.UploadImageService;
import com.kiosk.server.user.controller.dto.PatchProfileRequest;
import com.kiosk.server.user.controller.dto.PatchProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.ModifyUserProfileService;
import com.kiosk.server.user.util.UserProfileUtil;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModifyUserProfileServiceImpl implements ModifyUserProfileService {

    private final UserProfileUtil userProfileUtil;
    private final UserValidateUtil userValidateUtil;
    private final UserProfileRepository userProfileRepository;
    private final UploadImageService uploadImageService;
    private final DeleteImageService deleteImageService;

    @Override
    public PatchProfileResponse doService(long userId, long profileId, PatchProfileRequest request, MultipartFile image) {
        log.info("ModifyUserProfileService: userId={}, profileId={}", userId, profileId);

        // userProfile 조회
        UserProfile userProfile = fetchUserProfile(userId, profileId);

        ProfileRole role = userProfile.getProfileRole();
        log.info("조회된 프로필 - userId={}, profileId={}, role={}", userId, profileId, role);

        String updatedName = request.name() != null ? request.name() : userProfile.getProfileName();
        String updatedPhone = request.phone() != null ? request.phone() : userProfile.getPhoneNumber();

        // Role에 따라 필요한 필드를  처리
        Map<String, Object> updateProfileParam = userProfileUtil.createProfileParams(userId, profileId, updatedName, updatedPhone);

        // 역할에 따른 처리
        if (role.equals(ProfileRole.PARENT)) {
            userValidateUtil.validatePhoneNumber(updatedPhone);
            userValidateUtil.validateProfilePass(request.password());
            updateProfileParam.put("profilePass", request.password());
            userProfileRepository.updateParentProfile(updateProfileParam);
            log.info("부모 프로필 업데이트 완료 - userId={}, profileId={}", userId, profileId);

        } else if (role.equals(ProfileRole.CHILD)) {
            updateProfileParam.remove("phoneNumber", updatedPhone);
            userProfileRepository.updateChildProfile(updateProfileParam);
            log.info("자녀 프로필 업데이트 완료 - userId={}, profileId={}", userId, profileId);

        } else {
            log.warn("잘못된 프로필 역할 - userId={}, profileId={}, role={}", userId, profileId, role);
            throw new BadRequestException("프로필 역할이 선택되지 않았습니다. 올바른 프로필 역할을 선택해주세요.");
        }


        // 이미지 존재하면 이미지 저장
        if (image != null && !image.isEmpty()) {
            deleteImageService.doService(userId, profileId, "profile");
            uploadImageService.doService(userId, profileId, image, "profile");
            log.info("프로필 이미지 업데이트 완료 - userId={}, menuId={}", userId, profileId);
        }

        UserProfile user = userProfileUtil.getUserProfileById(userId, profileId);
        log.info("최종 수정된 프로필 - userId={}, profileId={}, role={}, name={}, phone={}",
                user.getUserId(), user.getProfileId(), user.getProfileRole(), user.getProfileName(), user.getPhoneNumber());

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
            log.warn("수정할 프로필을 찾을 수 없음 - userId={}, profileId={}", userId, profileId);
            throw new EntityNotFoundException("수정할 프로필이 선택되지 않았습니다. 수정할 프로필을 선택한 후 다시 시도해 주세요.");
        }
        return userProfile;
    }
}
