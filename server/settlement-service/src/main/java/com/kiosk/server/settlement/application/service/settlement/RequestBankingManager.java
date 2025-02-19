package com.kiosk.server.settlement.application.service.settlement;

import com.kiosk.server.infra.feign.client.BankingClient;
import com.kiosk.server.infra.feign.client.MenuClient;
import com.kiosk.server.infra.feign.dto.DepositRequestDTO;
import com.kiosk.server.settlement.domain.Menu;
import com.kiosk.server.settlement.domain.Receive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestBankingManager {

    private final BankingClient bankingClient;
    private final MenuClient menuClient;

    public Long requestDeposit(Receive receive) {

        return bankingClient.deposit(
                DepositRequestDTO.Request.builder()
                        .userId(receive.getShopId())
                        .amount(receive.getTotalPrice())
                        .build()
        ).orElseThrow();
    }

}
