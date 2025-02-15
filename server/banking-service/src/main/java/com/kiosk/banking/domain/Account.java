package com.kiosk.banking.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private Long accountId;
    private Long memberId;
    private String email;
    private Long balance;
    private String phoneNumber;
}
