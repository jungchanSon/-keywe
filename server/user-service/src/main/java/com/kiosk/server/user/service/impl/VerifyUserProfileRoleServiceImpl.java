package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.VerifyUserProfileRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyUserProfileRoleServiceImpl implements VerifyUserProfileRoleService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public boolean doService(long profileId, ProfileRole profileRole) {
        UserProfile profile = userProfileRepository.findByProfileId(profileId);
        return profile != null && profile.getProfileRole() == profileRole;
    }
}
