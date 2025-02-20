package com.kiosk.server.banking.adaptor.inbound.api.dto;

public class WithdrawRequestDTO {

    public record Withdraw(
            long userId,
            long amount
    ) { }
}
