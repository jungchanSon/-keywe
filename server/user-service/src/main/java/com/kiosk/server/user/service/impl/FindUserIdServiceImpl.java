package com.kiosk.server.user.service.impl;

import com.kiosk.server.user.domain.UserProfileRepository;
import com.kiosk.server.user.service.FindUserIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserIdServiceImpl implements FindUserIdService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<Long> doService(Long profileId) {
        return Optional.of(
                userProfileRepository.findByProfileId(profileId).getProfileId()
        );
    }
}
