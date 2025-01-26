package com.kiosk.server.user.service;

public interface CreateUserProfileService {

    long doService(long userId, String profileName, String profileRole, String phoneNumber, String profilePass);
}
