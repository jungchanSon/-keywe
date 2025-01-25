package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.LoginResponse;
import com.kiosk.server.user.controller.dto.UserLoginRequest;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserLoginService userLoginService;

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        String temporaryToken = "Bearer " + userLoginService.doService(request.email(), request.password());
        LoginResponse response = new LoginResponse(temporaryToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
