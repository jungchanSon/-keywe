package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.DeleteUserProfileService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserProfileServiceImpl implements DeleteUserProfileService {

    private final UserProfileUtil userProfileUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void doService(long userId, long originProfileId, long profileId) {
        log.info("DeleteUserProfileService: userId={}, originProfileId={}, profileId={}",
                userId, originProfileId, profileId);

        // profileId 검증
        if (originProfileId != profileId) {
            log.warn("프로필 ID 불일치. 요청 profileId={}, 원래 profileId={}", profileId, originProfileId);
            throw new UnauthorizedException("인증에 실패했습니다. 올바른 프로필로 다시 시도해 주세요.");
        }

        // userProfile 조회
        UserProfile userProfile = userProfileUtil.getUserProfileById(userId, originProfileId);
        if(userProfile == null) {
            log.warn("삭제할 프로필이 존재하지 않음. userId={}, profileId={}", userId, originProfileId);
            throw new EntityNotFoundException("해당 프로필을 찾을 수 없습니다. 올바른 프로필로 다시 시도해 주세요.");
        }

        // 삭제
        Map<String, Object> idParams = userProfileUtil.createIdParams(userId, originProfileId);
        userProfileRepository.deleteUserProfileById(idParams);

        log.info("사용자 프로필 삭제 완료. userId={}, profileId={}", userId, originProfileId);
    }
}
