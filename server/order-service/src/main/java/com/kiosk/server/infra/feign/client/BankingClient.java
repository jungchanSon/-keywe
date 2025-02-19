package com.kiosk.server.infra.feign.client;

import com.kiosk.server.infra.feign.dto.DepositRequestDTO;
import com.kiosk.server.infra.feign.dto.WithdrawRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(
        name = "banking-service",
        url  = "${feign.url.banking-service}"
)
public interface BankingClient {

    @PostMapping("/withdraw")
    Optional<Long> withdraw(WithdrawRequestDTO.Request request);


    @PostMapping("/deposit")
    Optional<Long> deposit(DepositRequestDTO.Request request);
}
