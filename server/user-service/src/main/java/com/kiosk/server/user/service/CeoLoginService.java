package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.CeoLoginResult;

public interface CeoLoginService {

    CeoLoginResult doService(String email, String password);
}
