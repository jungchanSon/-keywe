package com.kiosk.server.user.service;

public interface VerifyEmailService {

    void doService(String email, String authCode);
}
