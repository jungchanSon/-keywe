package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.inbound.DepositCommand;
import com.kiosk.server.banking.application.port.outbound.AccountCommandPort;
import com.kiosk.server.banking.application.port.outbound.DepositCommandPort;
import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.mapper.DepositMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositWriter implements DepositCommand {

    private final DepositCommandPort depositCommandPort;
    private final DepositMapper depositMapper;

    @Transactional
    public Long deposit(DepositVO.Save deposit) {
        Long depositRecordId = depositCommandPort.deposit(depositMapper.toDomain(deposit));

        log.info("[뱅킹] 입금 완료. depositRecordId={}, userId={}, amount={}", depositRecordId, deposit.userId(), deposit.amount());

        return  depositRecordId;
    }
}