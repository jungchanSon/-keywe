package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.UserProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.service.FindChildProfileListService;
import com.kiosk.server.user.service.FindUserIdService;
import com.kiosk.server.user.service.FindUserProfileService;
import com.kiosk.server.user.service.VerifyUserProfileRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final FindUserProfileService findUserProfileService;
    private final VerifyUserProfileRoleService verifyUserRoleService;
    private final FindUserIdService findUserIdService;
    private final FindChildProfileListService findChildProfileListService;

    @GetMapping("/users/profiles/{profileId}")
    public ResponseEntity<UserProfileResponse> findProfile(@PathVariable("profileId") Long profileId) {
        UserProfileResponse profile = findUserProfileService.doService(profileId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/users/{userId}/profiles/child")
    public ResponseEntity<List<UserProfileResponse>> findChildProfiles(@PathVariable("userId") Long userId) {
        List<UserProfileResponse> response = findChildProfileListService.doService(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/profiles/{profileId}/role/parent")
    public ResponseEntity<Boolean> verifyParentProfileRole(@PathVariable(name = "profileId") Long profileId) {
        return ResponseEntity.ok(verifyUserRoleService.doService(profileId, ProfileRole.PARENT));
    }

    @GetMapping("/userid/{profileId}")
    public Optional<Long> findUserIdByProfileId(@PathVariable("profileId") Long profileId) {
        return findUserIdService.doService(profileId);
    }
}
