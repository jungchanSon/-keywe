package com.kiosk.server.banking.application.service.banking.vo;

import lombok.Builder;

public class DepositVO {

    @Builder
    public record Save(
            Long userId,
            Long amount
    ) { }
}
