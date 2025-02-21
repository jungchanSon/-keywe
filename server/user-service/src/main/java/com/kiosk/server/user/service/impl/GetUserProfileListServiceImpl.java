package com.kiosk.server.user.service.impl;

import com.kiosk.server.image.repository.ProfileImageRepository;
import com.kiosk.server.user.controller.dto.UserProfileListResponse;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.GetUserProfileListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserProfileListServiceImpl implements GetUserProfileListService {

    private final UserProfileRepository userProfileRepository;
    private final ProfileImageRepository profileImageRepository;

    @Override
    public List<UserProfileListResponse> doService(long userId) {
        log.info("GetUserProfileListService: userId={}", userId);

        List<UserProfile> profileList = userProfileRepository.findByUserId(userId);
        if (profileList.isEmpty()) {
            log.info("조회된 프로필 없음 - userId={}", userId);
            return Collections.emptyList();
        }

        //프로필 ID리스트 추출
        List<Long> profileIds = new ArrayList<>();
        for (UserProfile profile : profileList) {
            profileIds.add(profile.getProfileId());
        }
        log.info("조회할 프로필 ID 목록: {}", profileIds);

        // 이미지 데이터 조회 (프로필별 이미지)
        Map<Long, byte[]> imageMap = getProfileImageMap(userId, profileIds);
        log.info("이미지 맵 크기: {}, 키 목록: {}", imageMap.size(), imageMap.keySet());

        // 기본 정보와 이미지 정보를 조합하여 DTO 변환
        List<UserProfileListResponse> profileResponses = new ArrayList<>();
        for (UserProfile profile : profileList) {
            byte[] imageBytes = imageMap.get(profile.getProfileId());
            log.info("프로필 ID: {} - 이미지 바이트 배열 존재 여부: {}, 길이: {}",
                    profile.getProfileId(),
                    (imageBytes != null),
                    (imageBytes != null ? imageBytes.length : 0)
            );
            String image = null;
            if (imageBytes != null && imageBytes.length > 0) {
                image = Base64.getUrlEncoder().withoutPadding().encodeToString(imageBytes);
                log.info("프로필 ID: {} - Base64 인코딩 완료, 길이: {}", profile.getProfileId(), image.length());
            }

            profileResponses.add(new UserProfileListResponse(
                    String.valueOf(profile.getProfileId()),
                    profile.getProfileName(),
                    profile.getProfileRole(),
                    image
            ));
        }

        log.info("프로필 조회 완료 - userId={}, profileCount={}", userId, profileResponses.size());
        return profileResponses;
    }

    private Map<Long, byte[]> getProfileImageMap(long userId, List<Long> profileIds) {
        Map<Long, byte[]> imageMap = new HashMap<>();
        for (Long profileId : profileIds) {
            Map<String, Object> params = new HashMap<>();
            params.put("targetId", profileId);
            params.put("userId", userId);

            byte[] imageBytes = (byte[]) profileImageRepository.findImageBytesById(params);
            log.info("프로필 이미지 조회 - profileId: {}, 이미지 존재: {}, 길이: {}",
                    profileId,
                    (imageBytes != null),
                    (imageBytes != null ? imageBytes.length : 0)
            );

            imageMap.put(profileId, imageBytes);
        }
        return imageMap;
    }
}
