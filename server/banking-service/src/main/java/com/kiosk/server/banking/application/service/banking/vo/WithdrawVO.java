package com.kiosk.server.banking.application.service.banking.vo;

public class WithdrawVO {

    public record Save(
            Long userId,
            Long amount
    ) { }
}
