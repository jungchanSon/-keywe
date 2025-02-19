package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.controller.dto.UserProfileResponse;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.FindUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindUserProfileServiceImpl implements FindUserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse doService(Long profileId) {
        return userProfileRepository.getUserProfile(profileId);
    }
}
