package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.WithdrawCommandPort;
import com.kiosk.server.banking.domain.Withdraw;
import com.kiosk.server.banking.domain.mapper.WithdrawMapper;
import com.kiosk.server.infra.rdb.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithdrawCommandAdaptor implements WithdrawCommandPort {

    private final WithdrawRepository withdrawRepository;

    private final WithdrawMapper withdrawMapper;

    @Override
    public Long withdraw(Withdraw domain) {
        return withdrawRepository.save(withdrawMapper.toEntity(domain)).getWithdrawEventId();
    }
}
