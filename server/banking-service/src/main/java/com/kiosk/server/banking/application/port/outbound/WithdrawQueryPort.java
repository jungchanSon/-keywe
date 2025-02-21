package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Withdraw;

import java.util.List;

public interface WithdrawQueryPort {

    Withdraw readWithdraw(Long userId);

    List<Withdraw> searchWithdrawList(Long userId, Long snapShot);
}
