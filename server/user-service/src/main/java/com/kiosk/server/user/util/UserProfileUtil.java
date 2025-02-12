package com.kiosk.server.user.util;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileUtil {

    private final UserProfileRepository userProfileRepository;

    // userId와 profileId를 기반으로 userProfile 조회
    public UserProfile getUserProfileById(long userId, long profileId) {

        if (userId <= 0 || profileId <= 0) {
            log.error("userId and profileId must be greater than 0");
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Map<String, Object> idParams = createIdParams(userId, profileId);

        UserProfile foundProfile = userProfileRepository.findUserProfileById(idParams);

        if (foundProfile == null) {
            throw new EntityNotFoundException("해당 사용자 프로필을 찾을 수 없습니다. 입력하신 정보를 다시 확인해 주세요.");
        }

        return foundProfile;
    }

    // idParams map 생성
    public Map<String, Object> createIdParams(long userId, long profileId) {
        Map<String, Object> idParams = new HashMap<>();
        idParams.put("userId", userId);
        idParams.put("profileId", profileId);
        return idParams;
    }

    // userParams map 생성
    public Map<String, Object> createProfileParams(long userId, long profileId, String profileName, String phoneNumber) {

        Map<String, Object> profileParams = new HashMap<>();
        profileParams.put("userId", userId);
        profileParams.put("profileId", profileId);
        profileParams.put("profileName", profileName);
        profileParams.put("phoneNumber", phoneNumber);
        return profileParams;
    }

}
