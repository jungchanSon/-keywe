package com.kiosk.server.infra.feign.client;

import com.kiosk.server.infra.feign.dto.DepositRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(
        name = "banking-service",
        url  = "${feign.url.banking-service}"
)
public interface BankingClient {

    @PostMapping("/deposit")
    Optional<Long> deposit(DepositRequestDTO.Request request);
}
