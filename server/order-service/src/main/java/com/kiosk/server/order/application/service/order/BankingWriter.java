package com.kiosk.server.order.application.service.order;

import com.kiosk.server.infra.feign.client.BankingClient;
import com.kiosk.server.order.application.port.outbound.BankingCommandPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankingWriter {

    private final BankingCommandPort bankingCommandPort;

    public Long withdraw(long userId, long totalPrice) {
        try {
            return bankingCommandPort.withdraw(userId, totalPrice);
        } catch (NoSuchElementException e) {
            log.error("[Order][주문] 출금 실패. 잔액 부족");
            throw new RuntimeException("출금에 실패했습니다. 잔액이 부족합니다.");
       }
    }

    public Long deposit(long userId, long totalprice) {
        return bankingCommandPort.withdraw(userId, totalprice);
    }
}
