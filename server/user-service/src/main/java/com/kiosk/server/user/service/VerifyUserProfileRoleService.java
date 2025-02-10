package com.kiosk.server.user.service;

import com.kiosk.server.user.domain.ProfileRole;

public interface VerifyUserProfileRoleService {

    boolean doService(long profileId, ProfileRole profileRole);
}
