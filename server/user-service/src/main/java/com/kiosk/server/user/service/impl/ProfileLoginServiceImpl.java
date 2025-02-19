package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.util.TokenUtil;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.service.ProfileLoginService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileLoginServiceImpl implements ProfileLoginService {

    private final TokenUtil tokenUtil;
    private final UserProfileUtil userProfileUtil;

    @Override
    public String doService(long userId, long profileId) {
        log.info("ProfileLoginService: userId={}, profileId={}", userId, profileId);

        // userProfile 조회
        UserProfile foundProfile = userProfileUtil.getUserProfileById(userId, profileId);

        log.info("조회된 프로필 - userId={}, profileId={}", foundProfile.getUserId(), foundProfile.getProfileId());

        return tokenUtil.createAuthenticationToken(foundProfile.getUserId(), foundProfile.getProfileId());
    }

}
