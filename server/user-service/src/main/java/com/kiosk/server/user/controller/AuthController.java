package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.*;
import com.kiosk.server.user.service.FindUserProfileByIdService;
import com.kiosk.server.user.service.KioskUserLoginService;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserLoginService userLoginService;
    private final KioskUserLoginService kioskUserLoginService;
    private final FindUserProfileByIdService findUserProfileByIdService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        log.info("Request Login email: {}", request.email());
        String temporaryToken = userLoginService.doService(request.email(), request.password());
        LoginResponse response = new LoginResponse(temporaryToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 프로필 선택
    @PostMapping("/user/profile")
    public ResponseEntity<ProfileLoginResponse> getUserProfile(@RequestHeader("userId") Long userId, @Validated @RequestBody UserProfileRequest request) {
        log.info("Request User Profile Id: {}", request.profileId());
        String authToken = findUserProfileByIdService.doService(userId, request.profileId());
        ProfileLoginResponse response = new ProfileLoginResponse(authToken, String.valueOf(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Token 생성 시 shopId 넣어야함
     */
    @PostMapping("/user/kiosk-login")
    public ResponseEntity<KioskUserLoginResult> loginParent(@RequestBody KioskUserLoginRequest request) {
        log.info("Request Phone Number: {}", request.phone());
        KioskUserLoginResult loginResult = kioskUserLoginService.doService(request.phone(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(loginResult);
    }

}
