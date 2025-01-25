package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.UserLoginRequest;
import com.kiosk.server.user.controller.dto.LoginResponse;
import com.kiosk.server.user.controller.dto.RegisterUserRequest;
import com.kiosk.server.user.service.RegisterUserService;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserService registerUserService;
    private final UserLoginService userLoginService;

    @PostMapping("/user")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request) {
        registerUserService.doService(request.email(), request.password(), request.role());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest request) {
        String temporaryToken = userLoginService.doService(request.email(), request.password());
        LoginResponse response = new LoginResponse(temporaryToken, "Bearer");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
