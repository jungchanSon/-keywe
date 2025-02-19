package com.kiosk.server.infra.feign.dto;

import lombok.Builder;

public class WithdrawRequestDTO {

    @Builder
    public record Request(
            long userId,
            long amount
    ) { }

}
