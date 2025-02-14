package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.PatchProfileRequest;
import com.kiosk.server.user.controller.dto.PatchProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ModifyUserProfileService {

    PatchProfileResponse doService (long userId, long profileId, PatchProfileRequest request, MultipartFile image);
}
