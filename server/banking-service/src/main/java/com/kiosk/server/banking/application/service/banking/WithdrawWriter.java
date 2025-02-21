package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.inbound.WithdrawCommand;
import com.kiosk.server.banking.application.port.outbound.WithdrawCommandPort;
import com.kiosk.server.banking.application.service.banking.vo.WithdrawVO;
import com.kiosk.server.banking.domain.Account;
import com.kiosk.server.banking.domain.mapper.WithdrawMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawWriter implements WithdrawCommand {

    private final WithdrawCommandPort withdrawCommandPort;
    private final AccountWriter accountWriter;
    private final WithdrawMapper withdrawMapper;
    private final AccountService accountService;

    @Override
    @Transactional
    public Long withdraw(WithdrawVO.Save withDraw) {
        Account account = accountService.getAccount(withDraw.userId());

        if(account.getBalance() < withDraw.amount()) {
            log.info("[뱅킹] 출금 불가. 잔액 부족. userId={}, balance={}, request={}", withDraw.userId(), account.getBalance(), withDraw.amount());
            throw new RuntimeException("계좌 잔액 부족");
        }

        accountWriter.minus(withDraw.userId(), withDraw.amount());

        Long withdrawId = withdrawCommandPort.withdraw(withdrawMapper.toDomain(withDraw));
        log.info("[뱅킹] 출금 완료. withdrawId={}, userId={}, amount={}", withdrawId, withDraw.userId(), withDraw.amount());
        return  withdrawId;
    }
}
