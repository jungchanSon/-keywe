package com.kiosk.server.banking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BalanceHistory {

    private Long balanceHistoryId;

    private Long fromUserId;

    private Long toUserId;

    private Long amount;

    private LocalDateTime createdAt;
}
