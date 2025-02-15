package com.kiosk.banking.adaptor.outbound;

import com.kiosk.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.banking.domain.Account;
import com.kiosk.banking.domain.mapper.AccountMapper;
import com.kiosk.infra.rdb.entity.AccountEntity;
import com.kiosk.infra.rdb.repository.AccountRepository;
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
