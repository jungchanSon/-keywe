package com.kiosk.server.settlement.application.service.settlement.vo;

public class SettlementVO {

    public record Save(
            Long memberId,
            String email
    ) { }
}
