package com.kiosk.server.infra.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "menu-service",
        url  = "${feign.url.menu-service}"
)
public interface MenuClient {

    @GetMapping("/internal/menu/{menuId}")
    Optional<Integer> requestMenuPrice(@PathVariable Long menuId);

    @GetMapping("/internal/option/{menuId}/{optionId}")
    Optional<Integer> requestOptionPrice(@PathVariable Long menuId, @PathVariable Long optionId);

}
