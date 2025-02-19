package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Deposit;

public interface DepositCommandPort {

    Long deposit(Deposit deposit);
}
