package com.kiosk.server.banking.application.service.banking.vo;

public class AccountVO {

    public record Save(
            Long memberId,
            String email
    ) { }
}
