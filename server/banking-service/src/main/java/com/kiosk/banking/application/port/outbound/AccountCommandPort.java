package com.kiosk.banking.application.port.outbound;

import com.kiosk.banking.domain.Account;

public interface AccountCommandPort {
    long create(Account account);
}
