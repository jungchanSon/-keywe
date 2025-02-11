package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.controller.dto.ChildProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.FindChildProfileListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindChildProfileListServiceImpl implements FindChildProfileListService {

    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChildProfileResponse> doService(long userId) {
        return userProfileRepository.getUserProfileList(userId)
            .stream()
            .filter(profile -> ProfileRole.CHILD == profile.type())
            .map(profile -> new ChildProfileResponse(String.valueOf(profile.id()), profile.name()))
            .toList();
    }
}
