package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.controller.dto.UserProfileDetailResponse;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.service.ProfileDetailService;
import com.kiosk.server.user.util.UserProfileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileDetailServiceImpl implements ProfileDetailService {

    private final UserProfileUtil userProfileUtil;

    @Override
    public UserProfileDetailResponse doService(long userId, long profileId) {

        UserProfile userProfile = userProfileUtil.getUserProfileById(userId, profileId);

        return new UserProfileDetailResponse(profileId, userProfile.getProfileName(), userProfile.getPhoneNumber());
    }
}
