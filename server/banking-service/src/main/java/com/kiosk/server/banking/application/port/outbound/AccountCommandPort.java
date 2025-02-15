package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Account;

public interface AccountCommandPort {
    long create(Account account);
}
