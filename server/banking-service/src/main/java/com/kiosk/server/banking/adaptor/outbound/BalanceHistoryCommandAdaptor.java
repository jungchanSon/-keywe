package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.BalanceHistoryCommandPort;
import com.kiosk.server.banking.domain.BalanceHistory;
import com.kiosk.server.banking.domain.mapper.BalanceHistoryMapper;
import com.kiosk.server.infra.rdb.repository.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceHistoryCommandAdaptor implements BalanceHistoryCommandPort {

    private final BalanceHistoryRepository balanceHistoryRepository;

    private final BalanceHistoryMapper balanceHistoryMapper;

    @Override
    public Long recordTransaction(BalanceHistory balanceHistory) {
        return balanceHistoryRepository.save(
                balanceHistoryMapper.toEntity(balanceHistory)
        ).getBalanceHistoryId();
    }
}
