package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.VerifyUserProfileRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyUserProfileRoleServiceImpl implements VerifyUserProfileRoleService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public boolean doService(long profileId, ProfileRole profileRole) {
        log.info("VerifyUserProfileRoleService: profileId={}, profileRole={}", profileId, profileRole);

        UserProfile profile = userProfileRepository.findByProfileId(profileId);
        return profile != null && profile.getProfileRole() == profileRole;
    }
}
