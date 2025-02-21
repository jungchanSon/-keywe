package com.kiosk.server.banking.adaptor.inbound.api.dto;

public class DepositRequestDTO {

    public record Deposit(
            Long userId,
            Long amount
    ) { }
}
