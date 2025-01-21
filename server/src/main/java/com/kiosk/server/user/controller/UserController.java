package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.RegisterRequest;
import com.kiosk.server.user.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserService registerUserService;

    @PostMapping("/user")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        registerUserService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
