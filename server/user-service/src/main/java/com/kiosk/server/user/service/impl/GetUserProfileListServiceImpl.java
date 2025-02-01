package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.controller.dto.UserProfileResponse;
import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.GetUserProfileListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserProfileListServiceImpl implements GetUserProfileListService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfileResponse> doService(long userId) {

        return userProfileRepository.getUserProfileList(userId);
    }

}
