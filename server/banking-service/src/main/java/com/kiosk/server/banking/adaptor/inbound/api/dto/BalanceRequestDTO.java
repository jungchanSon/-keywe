package com.kiosk.server.banking.adaptor.inbound.api.dto;

public class BalanceRequestDTO {

    public record Save(
            Long fromUserId,
            Long toUserId,
            Long amount
    ) { }
}
