package com.kiosk.server.banking.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Account {
    private Long accountId;
    private Long memberId;
    private String email;
    private Long balance;
    private String phoneNumber;
    private Long withdrawSnapshotId;
    private Long depositSnapshotId;

}
