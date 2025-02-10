package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.*;
import com.kiosk.server.user.service.FindUserProfileByIdService;
import com.kiosk.server.user.service.KioskUserLoginService;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserLoginService userLoginService;
    private final KioskUserLoginService kioskUserLoginService;
    private final FindUserProfileByIdService findUserProfileByIdService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        String temporaryToken = userLoginService.doService(request.email(), request.password());
        LoginResponse response = new LoginResponse(temporaryToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 프로필 선택
    @PostMapping("/user/profile")
    public ResponseEntity<ProfileLoginResponse> getUserProfile(@RequestHeader("userId") Long userId, @Validated @RequestBody UserProfileRequest request) {
        String authToken = findUserProfileByIdService.doService(userId, request.profileId());
        ProfileLoginResponse response = new ProfileLoginResponse(authToken, String.valueOf(userId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/user/kiosk-login")
    public ResponseEntity<LoginResponse> loginParent(@RequestBody KioskUserLoginRequest request) {
        String authToken = kioskUserLoginService.doService(request.phone(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(authToken));
    }

}
