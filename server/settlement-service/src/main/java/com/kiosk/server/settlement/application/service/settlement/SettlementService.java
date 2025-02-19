package com.kiosk.server.settlement.application.service.settlement;

import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import com.kiosk.server.settlement.application.port.inbound.SettlementManager;
import com.kiosk.server.settlement.domain.Receive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService implements SettlementManager {

    private final ApplicationEventPublisher eventPublisher;
    private final RequestBankingManager requestBankingManager;

    @Override
    @Transactional
    public void settle(Receive receive) {

        eventPublisher.publishEvent(receive);

        requestBankingManager.requestDeposit(receive);
        receive.setOutboxStatus(OutBoxStatus.COMPLETED);
        eventPublisher.publishEvent(receive);
    }
}
