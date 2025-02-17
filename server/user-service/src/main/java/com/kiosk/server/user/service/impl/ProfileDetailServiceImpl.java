package com.kiosk.server.user.service.impl;

import com.kiosk.server.image.repository.ProfileImageRepository;
import com.kiosk.server.user.controller.dto.UserProfileDetailResponse;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.service.ProfileDetailService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileDetailServiceImpl implements ProfileDetailService {

    private final UserProfileUtil userProfileUtil;
    private final ProfileImageRepository imageRepository;

    @Override
    public UserProfileDetailResponse doService(long userId, long profileId) {
        log.info("ProfileDetailService: userId={}, profileId={}", userId, profileId);

        UserProfile userProfile = userProfileUtil.getUserProfileById(userId, profileId);

        // 프로필 이미지 조회 & Base64 변환
        Map<String, Object> params = new HashMap<>();
        params.put("targetId", profileId);
        params.put("userId", userId);

        byte[] imageBytes = (byte[]) imageRepository.findImageBytesById(params);
        String image = (imageBytes != null && imageBytes.length > 0)
                ? "data:image/png;base64," + Base64.getUrlEncoder().withoutPadding().encodeToString(imageBytes)
                : null;

        log.info("조회된 프로필 - userId={}, profileId={}, name={}, role={}, phone={}",
                userProfile.getUserId(), userProfile.getProfileId(),
                userProfile.getProfileName(), userProfile.getProfileRole(), userProfile.getPhoneNumber());

        return new UserProfileDetailResponse(
                String.valueOf(userProfile.getProfileId()),
                userProfile.getProfileName(),
                userProfile.getProfileRole(),
                userProfile.getPhoneNumber(),
                image);
    }
}
