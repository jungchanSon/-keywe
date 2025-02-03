package com.kiosk.server.user.service;

public interface DeleteUserProfileService {

    void doService(long userId, long originProfileId, long profileId);
}
