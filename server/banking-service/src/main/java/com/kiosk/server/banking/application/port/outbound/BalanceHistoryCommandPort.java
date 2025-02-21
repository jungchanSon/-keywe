package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.BalanceHistory;

public interface BalanceHistoryCommandPort {
    Long recordTransaction(BalanceHistory balanceHistory);
}
