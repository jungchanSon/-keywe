package com.kiosk.server.client.feign.api;

import com.kiosk.server.client.feign.dto.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "userClient", url = "${service.user}")
public interface UserClient {

    @GetMapping("/internal/users/{userId}/profiles/child")
    List<UserProfile> getHelperProfiles(@PathVariable(name = "userId") String userId);

    @GetMapping(value = "/internal/profiles/{userId}/role/parent")
    boolean verifyParentRole(@PathVariable("userId") String userId);
}
