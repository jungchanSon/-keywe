package com.kiosk.server.banking.application.port.inbound;

import com.kiosk.server.banking.domain.Account;

public interface AccountUseCase {
    Account getAccount(Long memberId);

}
