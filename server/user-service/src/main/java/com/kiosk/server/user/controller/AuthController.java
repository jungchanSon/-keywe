package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.LoginResponse;
import com.kiosk.server.user.controller.dto.ProfileTokenResponse;
import com.kiosk.server.user.controller.dto.UserLoginRequest;
import com.kiosk.server.user.controller.dto.UserProfileRequest;
import com.kiosk.server.user.service.FindUserProfileByIdService;
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
    private final FindUserProfileByIdService findUserProfileByIdService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        String temporaryToken = userLoginService.doService(request.email(), request.password());
        LoginResponse response = new LoginResponse(temporaryToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 프로필 선택
    @PostMapping("/user/profile")
    public ResponseEntity<ProfileTokenResponse> getUserProfile(@RequestHeader("userId") Long userId, @Validated @RequestBody UserProfileRequest request) {
        String authToken = findUserProfileByIdService.doService(userId, request.profileId());
        ProfileTokenResponse response = new ProfileTokenResponse(authToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
