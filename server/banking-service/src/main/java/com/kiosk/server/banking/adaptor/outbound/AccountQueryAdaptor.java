package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.AccountQueryPort;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.mapper.AccountMapper;
import com.kiosk.server.infra.rdb.entity.AccountEntity;
import com.kiosk.server.infra.rdb.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountQueryAdaptor implements AccountQueryPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account getAccountByUserId(Long userId) {
        AccountEntity byMemberId = accountRepository.findByMemberId(userId);
        List<AccountEntity> accountEntitiesByMemberId = accountRepository.getAccountEntitiesByMemberId(userId);
        accountEntitiesByMemberId.forEach(account -> {

        });
        return accountMapper.toDomainFromEntity(accountRepository.findByMemberId(userId));
    }
}
