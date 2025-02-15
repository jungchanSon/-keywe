package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.mapper.AccountMapper;
import com.kiosk.server.infra.rdb.entity.AccountEntity;
import com.kiosk.server.infra.rdb.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCommandAdaptor implements AccountCommandPort {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Override
    public long create(Account account) {
        AccountEntity savedAccount = accountRepository.save(accountMapper.toEntity(account));

        return savedAccount.getAccountId();
    }
}
