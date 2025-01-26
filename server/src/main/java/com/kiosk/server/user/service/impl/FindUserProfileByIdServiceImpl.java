package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.util.TokenUtil;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.service.FindUserProfileByIdService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserProfileByIdServiceImpl implements FindUserProfileByIdService {

    private final TokenUtil tokenUtil;
    private final UserProfileUtil userProfileUtil;

    @Override
    public String doService(long userId, long profileId) {

        // userProfile 조회
        UserProfile foundProfile = userProfileUtil.getUserProfileById(userId, profileId);

        return tokenUtil.createAuthenticationToken(foundProfile.getUserId(), foundProfile.getProfileId());
    }

}
