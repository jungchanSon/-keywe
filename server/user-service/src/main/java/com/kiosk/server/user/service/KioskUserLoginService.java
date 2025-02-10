package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.KioskUserLoginResult;

public interface KioskUserLoginService {

    KioskUserLoginResult doService(String phone, String password);
}
