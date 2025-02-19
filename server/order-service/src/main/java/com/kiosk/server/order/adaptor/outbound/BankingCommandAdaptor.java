package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.infra.feign.client.BankingClient;
import com.kiosk.server.infra.feign.dto.DepositRequestDTO;
import com.kiosk.server.infra.feign.dto.WithdrawRequestDTO;
import com.kiosk.server.order.application.port.outbound.BankingCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankingCommandAdaptor implements BankingCommandPort {

    private final BankingClient bankingClient;

    @Override
    public Long withdraw(long userId, long totalPrice) {
        WithdrawRequestDTO.Request withdrawRequest = WithdrawRequestDTO.Request.builder()
                .userId(userId)
                .amount(totalPrice)
                .build();
        return bankingClient.withdraw(withdrawRequest).orElseThrow();
    }

    @Override
    public Long deposit(long userId, long totalPrice) {
        DepositRequestDTO.Request depositRequest = DepositRequestDTO.Request.builder()
                .userId(userId)
                .amount(totalPrice)
                .build();

        return bankingClient.deposit(depositRequest).orElseThrow();
    }
}
