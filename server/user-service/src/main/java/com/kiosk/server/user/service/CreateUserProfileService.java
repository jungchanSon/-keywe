package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.CreateProfileRequest;
import com.kiosk.server.user.controller.dto.CreateProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CreateUserProfileService {

    CreateProfileResponse doService(long userId, CreateProfileRequest request, MultipartFile image);
}
