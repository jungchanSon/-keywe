package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.DeleteUserProfileService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeleteUserProfileServiceImpl implements DeleteUserProfileService {

    private final UserProfileUtil userProfileUtil;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void doService(long userId, long originProfileId, long profileId) {

        // profileId 검증
        if (originProfileId != profileId) {
            throw new UnauthorizedException("Unauthorized ProfileId");
        }

        // userProfile 조회
        UserProfile userProfile = userProfileUtil.getUserProfileById(userId, originProfileId);
        if(userProfile == null) {
            throw new EntityNotFoundException("User Profile Not Found");
        }

        // 삭제
        Map<String, Object> idParams = userProfileUtil.createIdParams(userId, originProfileId);

        userProfileRepository.deleteUserProfileById(idParams);

    }
}
