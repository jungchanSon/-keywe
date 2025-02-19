package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Account;

public interface AccountQueryPort {
    Account getAccountByUserId(Long userId);
}
