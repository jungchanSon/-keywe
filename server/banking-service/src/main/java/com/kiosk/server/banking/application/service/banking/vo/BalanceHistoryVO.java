package com.kiosk.server.banking.application.service.banking.vo;

public class BalanceHistoryVO {

     public record Save(
            Long fromUserId,
            Long toUserId,
            Long amount
     ){ }
}
