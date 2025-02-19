package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Withdraw;

public interface WithdrawCommandPort {

    Long withdraw(Withdraw domain);
}
