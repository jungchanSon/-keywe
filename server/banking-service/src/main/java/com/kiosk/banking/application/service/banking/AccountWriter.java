package com.kiosk.banking.application.service.banking;

import com.kiosk.banking.application.port.inbound.AccountCommand;
import com.kiosk.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.banking.application.service.banking.vo.AccountVO;
import com.kiosk.banking.domain.mapper.AccountMapper;
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
}
