package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Deposit;

import java.util.List;

public interface DepositQueryPort {

    Deposit readDeposit(Long userId);

    List<Deposit> searchDepositList(Long snapShot, Long userId);


}

