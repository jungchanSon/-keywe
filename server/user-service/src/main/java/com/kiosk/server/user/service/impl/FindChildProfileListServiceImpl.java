package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.controller.dto.UserProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.FindChildProfileListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindChildProfileListServiceImpl implements FindChildProfileListService {

    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileResponse> doService(long userId) {
        log.info("FindChildProfileListService: userId={}", userId);

        return userProfileRepository.getUserProfileList(userId)
            .stream()
            .filter(profile -> ProfileRole.CHILD == profile.role())
            .toList();
    }
}
