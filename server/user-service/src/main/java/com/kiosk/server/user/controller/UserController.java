package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.*;
import com.kiosk.server.user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
        log.info("request: email={}", request.email());
        registerUserService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 프로필 목록 조회
    @GetMapping("/profile/list")
    public ResponseEntity<List<UserProfileResponse>> getUserProfileList(@RequestHeader("userId") Long userId) {
        log.info("request: userId={}", userId);
        List<UserProfileResponse> userProfileList = getUserProfileListService.doService(userId);
        log.info("response: userId={}, profilesCount={}", userId, userProfileList.size());
        return ResponseEntity.ok(userProfileList);
    }

    // 프로필 상세조회
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDetailResponse> getUserProfile(@RequestHeader("userId") Long userId, @RequestHeader("profileId") Long profileId) {
        log.info("request: userId={}, profileId={}", userId, profileId);
        UserProfileDetailResponse response = profileDetailService.doService(userId, profileId);
        log.info("response: profileId={}, name={}, role={}", profileId, response.name(), response.role());
        return ResponseEntity.ok(response);
    }

    // 프로필 생성
    @PostMapping("/profile")
    public ResponseEntity<CreateProfileResponse> createProfile(@RequestHeader("userId") Long userId, @RequestBody CreateProfileRequest request) {
        log.info("request: userId={}, name={}, role={}", userId, request.name(), request.role());
        CreateProfileResponse response = createUserProfileService.doService(userId, request.name(), request.role(), request.phone(), request.password());
        log.info("response: userId={}, profileId={}, createdAt={}", userId, response.id(),response.createAt());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<PatchProfileResponse> modifyUserProfile(
            @RequestHeader("userId") Long userId,
            @RequestHeader("profileId") Long profileId,
            @RequestBody PatchProfileRequest request
    ) {
        log.info("request: userId={}, profileId={}, name={}, phone={}", userId, profileId, request.name(), request.phone());
        PatchProfileResponse response = modifyUserProfileService.doService(userId, profileId, request.name(), request.phone(), request.password());
        log.info("response:profileId={}, name={}, phone={}", profileId, response.name(), response.phone());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로필 삭제
    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUserProfile(
            @RequestHeader("userId") Long userId,
            @RequestHeader("profileId") Long originProfileId,
            @Validated @RequestBody UserProfileRequest request
    ) {
        log.info("request: userId={}, originProfileId={}, targetProfileId={}", userId, originProfileId, request.profileId());
        deleteUserProfileService.doService(userId, originProfileId, request.profileId());
        log.info("response: profileId={}", request.profileId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

