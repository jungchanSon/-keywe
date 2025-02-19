package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.*;
import com.kiosk.server.user.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CeoLoginService ceoLoginService;
    private final UserLoginService userLoginService;
    private final VerifyEmailService verifyEmailService;
    private final ProfileLoginService profileLoginService;
    private final KioskUserLoginService kioskUserLoginService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        log.info("Request Login email: {}", request.email());
        LoginResponse response = userLoginService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 프로필 선택
    @PostMapping("/user/profile")
    public ResponseEntity<ProfileLoginResponse> selectUserProfile(
        @RequestHeader("userId") Long userId,
        @Validated @RequestBody UserProfileRequest request
    ) {
        log.info("Profile Login Id: {}", request.profileId());
        String authToken = profileLoginService.doService(userId, request.profileId());
        ProfileLoginResponse response = new ProfileLoginResponse(authToken);
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

    @PostMapping("/ceo/login")
    public ResponseEntity<CeoLoginResult> ceoLogin(@RequestBody UserLoginRequest request) {
        log.info("Request Ceo Login email: {}", request.email());
        CeoLoginResult ceoLoginResult = ceoLoginService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(ceoLoginResult);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
        @RequestParam String email,
        @RequestParam String token
    ) {
        EmailVerificationTemplate template;
        try {
            verifyEmailService.doService(email, token);
            template = EmailVerificationTemplate.SUCCESS;
        } catch (Exception e) {
            template = EmailVerificationTemplate.FAILURE;
        }

        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(template.getTemplate());
    }
}
