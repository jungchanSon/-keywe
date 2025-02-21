package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.inbound.AccountCommand;
import com.kiosk.server.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.server.banking.application.service.banking.vo.AccountVO;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountWriter implements AccountCommand {

    private final AccountCommandPort accountCommandPort;

    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public long create(AccountVO.Save account) {
        return accountCommandPort.create(accountMapper.toDomain(account));
    }

    @Override
    @Transactional
    public Long plus(long id, long amount) {
        return accountCommandPort.plus(id, amount);
    }

    @Override
    @Transactional
    public Long minus(long id, long amount) {
        return accountCommandPort.minus(id, amount);
    }
}
