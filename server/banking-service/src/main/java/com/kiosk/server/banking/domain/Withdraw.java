package com.kiosk.server.banking.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Withdraw {
    private Long withdrawEventId;
    private Long userId;
    private Long amount;
    private Long remainingBalance;
    private LocalDateTime createdAt;
}
