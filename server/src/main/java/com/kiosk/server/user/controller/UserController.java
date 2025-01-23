package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.LoginRequest;
import com.kiosk.server.user.controller.dto.RegisterRequest;
import com.kiosk.server.user.service.RegisterUserService;
import com.kiosk.server.user.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserService registerUserService;
    private final UserLoginService userLoginService;

    @PostMapping("/user")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        registerUserService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String temporaryToken = userLoginService.doService(request.email(), request.password());
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", temporaryToken);
        response.put("tokenType", "Bearer");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
