package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.ChildProfileResponse;
import com.kiosk.server.user.domain.ProfileRole;
import com.kiosk.server.user.service.FindChildProfileListService;
import com.kiosk.server.user.service.VerifyUserProfileRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class InternalController {

    private final FindChildProfileListService findChildProfileListService;
    private final VerifyUserProfileRoleService verifyUserRoleService;

    @GetMapping("/users/{userId}/profiles/child")
    public ResponseEntity<List<ChildProfileResponse>> findChildProfiles(@PathVariable("userId") Long userId) {
        List<ChildProfileResponse> response = findChildProfileListService.doService(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/profiles/{profileId}/role/parent")
    public ResponseEntity<Boolean> verifyParentProfileRole(@PathVariable(name = "profileId") Long profileId) {
        return ResponseEntity.ok(verifyUserRoleService.doService(profileId, ProfileRole.PARENT));
    }
}
