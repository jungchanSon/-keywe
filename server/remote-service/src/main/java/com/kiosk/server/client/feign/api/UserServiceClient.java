package com.kiosk.server.client.feign.api;

import com.kiosk.server.client.feign.dto.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "userClient", url = "${service.user}")
public interface UserServiceClient {

    @GetMapping("/internal/users/profiles/{userId}")
    UserProfile getUserProfile(@PathVariable(name = "userId") String userId);

    @GetMapping("/internal/users/{familyId}/profiles/child")
    List<UserProfile> getHelperProfiles(@PathVariable(name = "familyId") String familyId);

}
