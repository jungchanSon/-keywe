package com.kiosk.server.banking.application.port.outbound;

import com.kiosk.server.banking.domain.Account;

public interface AccountCommandPort {
    long create(Account account);

    Long plus(long id, long amount);

    Long minus(long id, long amount);

    Long update(Account account);
}
