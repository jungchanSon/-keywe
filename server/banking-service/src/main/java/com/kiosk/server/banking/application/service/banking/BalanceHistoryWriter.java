package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.inbound.BalanceHistoryCommand;
import com.kiosk.server.banking.application.port.outbound.BalanceHistoryCommandPort;
import com.kiosk.server.banking.application.service.banking.vo.BalanceHistoryVO;
import com.kiosk.server.banking.domain.mapper.BalanceHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceHistoryWriter implements BalanceHistoryCommand {

    private final BalanceHistoryCommandPort balanceHistoryCommandPort;

    private final BalanceHistoryMapper balanceHistoryMapper;

    @Transactional
    public Long recordTransaction(BalanceHistoryVO.Save balanceHistory) {
        return balanceHistoryCommandPort.recordTransaction(balanceHistoryMapper.toDomain(balanceHistory));
    }
}
