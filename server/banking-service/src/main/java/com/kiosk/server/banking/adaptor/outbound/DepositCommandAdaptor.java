package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.DepositCommandPort;
import com.kiosk.server.banking.domain.Deposit;
import com.kiosk.server.banking.domain.mapper.DepositMapper;
import com.kiosk.server.infra.rdb.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositCommandAdaptor implements DepositCommandPort {

    private final DepositRepository depositRepository;

    private final DepositMapper depositMapper;

    @Override
    public Long deposit(Deposit deposit) {
        return depositRepository.save(depositMapper.toEntity(deposit)).getDepositEventId();
    }
}
