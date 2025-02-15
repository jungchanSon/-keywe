package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.image.service.UploadImageService;
import com.kiosk.server.user.controller.dto.CreateProfileRequest;
import com.kiosk.server.user.controller.dto.CreateProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.CreateUserProfileService;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserProfileServiceImpl implements CreateUserProfileService {

    private final UserValidateUtil userValidateUtil;
    private final UserProfileRepository userProfileRepository;
    private final UploadImageService uploadImageService;

    @Override
    public CreateProfileResponse doService(long userId, CreateProfileRequest request, MultipartFile image) {
        log.info("CreateUserProfileService: userId={}, profileName={}, profileRole={}",
                userId, request.name(), request.role());

        // null 혹은 빈 값 유효성 검사
        userValidateUtil.validateName(userId, request.name());

        // profileRole로 전환
        ProfileRole role = convertToProfileRole(request.role());

        UserProfile newProfile;

        if (role == ProfileRole.CHILD) {
            newProfile = UserProfile.createChild(userId, request.name(), role, request.phone());
        } else if (role == ProfileRole.PARENT) {
            userValidateUtil.validatePhoneNumber(request.phone());
            userValidateUtil.validateProfilePass(request.password());
            newProfile = UserProfile.createParent(userId, request.name(), role, request.phone(), request.password());
        } else {
            log.warn("잘못된 프로필 역할 지정. userId={}, profileRole={}", userId, request.role());
            throw new BadRequestException("프로필 역할이 올바르지 않습니다. 올바른 역할을 선택해 주세요.");
        }

        userProfileRepository.createNewProfile(newProfile);

        // 이미지 존재하면 이미지 저장
        if (image != null && !image.isEmpty()) {
            uploadImageService.doService(userId, newProfile.getProfileId(), image, "profile");
            log.info("이미지 업로드 완료. userId={}, profileId={}", userId, newProfile.getProfileId());
        }

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
