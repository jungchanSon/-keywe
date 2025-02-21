package com.kiosk.server.infra.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "user-service",
        url  = "${feign.url.user-service}"
)
public interface UserClient {

    @GetMapping("/internal/userid/{profileId}")
    Optional<Long> getUserId(@PathVariable("profileId") Long profileId);

}
