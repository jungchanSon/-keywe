package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.*;
import com.kiosk.server.user.domain.UserProfile;
import com.kiosk.server.user.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserProfileService createUserProfileService;
    private final ProfileDetailService profileDetailService;
    private final GetUserProfileListService getUserProfileListService;
    private final ModifyUserProfileService modifyUserProfileService;
    private final DeleteUserProfileService deleteUserProfileService;
    private final RegisterUserService registerUserService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request) {
        registerUserService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 프로필 목록 조회
    @GetMapping("/profile/list")
    public ResponseEntity<List<UserProfileResponse>> getUserProfileList(@RequestHeader("userId") Long userId) {
        List<UserProfileResponse> userProfileList = getUserProfileListService.doService(userId);
        return ResponseEntity.ok(userProfileList);
    }

    // 프로필 상세조회
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<UserProfileDetailResponse> getUserProfile(@RequestHeader("userId") Long userId, @PathVariable long profileId) {
        UserProfileDetailResponse response = profileDetailService.doService(userId, profileId);
        return ResponseEntity.ok(response);
    }

    // 프로필 생성
    @PostMapping("/profile")
    public ResponseEntity<CreateProfileResponse> createProfile(@RequestHeader("userId") Long userId, @RequestBody CreateProfileRequest request) {
        long profileId = createUserProfileService.doService(userId, request.name(), request.type(), request.phone(), request.password());
        CreateProfileResponse response = new CreateProfileResponse(profileId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 프로필 수정
    @PatchMapping("/profile/{profileId}")
    public ResponseEntity<PatchProfileResponse> modifyUserProfile(
        @RequestHeader("userId") Long userId,
        @RequestHeader("profileId") Long profileId,
        @RequestBody PatchProfileRequest request
    ) {
        UserProfile userProfile = modifyUserProfileService.doService(userId, profileId, request.name(), request.phone(), request.password());
        PatchProfileResponse response = new PatchProfileResponse(userProfile.getProfileName(), userProfile.getPhoneNumber(), userProfile.getProfilePass());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로필 삭제조회
    @DeleteMapping("/profile/{profileId}")
    public ResponseEntity<Void> deleteUserProfile(
        @RequestHeader("userId") Long userId,
        @RequestHeader("profileId") Long originProfileId,
        @PathVariable long profileId
    ) {
        deleteUserProfileService.doService(userId, originProfileId, profileId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
