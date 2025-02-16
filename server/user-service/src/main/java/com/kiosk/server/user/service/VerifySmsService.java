package com.kiosk.server.user.service;

public interface VerifySmsService {

    void doService(String phone, String verificationCode);
}
