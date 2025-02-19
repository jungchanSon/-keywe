package com.kiosk.server.infra.feign.dto;

import lombok.Builder;

public class DepositRequestDTO {

    @Builder
    public record Request(
            long userId,
            long amount
    ) { }
}
