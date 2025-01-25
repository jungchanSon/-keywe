package com.kiosk.server.user.controller;

import com.kiosk.server.user.controller.dto.RegisterUserRequest;
import com.kiosk.server.user.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserService registerUserService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest request) {
        registerUserService.doService(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
