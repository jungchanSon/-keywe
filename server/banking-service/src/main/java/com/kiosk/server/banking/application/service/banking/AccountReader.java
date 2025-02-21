package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.outbound.AccountQueryPort;
import com.kiosk.server.banking.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountReader {

    private final AccountQueryPort accountQueryPort;

    public Account readAccount(Long userId) {
        return accountQueryPort.getAccountByUserId(userId);
    }
}
