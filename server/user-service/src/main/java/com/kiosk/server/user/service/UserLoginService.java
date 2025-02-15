package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.LoginResponse;

public interface UserLoginService {

    LoginResponse doService(String email, String password);
}
