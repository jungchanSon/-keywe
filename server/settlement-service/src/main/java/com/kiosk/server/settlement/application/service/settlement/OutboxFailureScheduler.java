package com.kiosk.server.settlement.application.service.settlement;

import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import com.kiosk.server.settlement.adaptor.outbound.ReceiveCommandAdaptor;
import com.kiosk.server.settlement.adaptor.outbound.ReceiveQueryAdaptor;
import com.kiosk.server.settlement.domain.Receive;
import com.kiosk.server.settlement.domain.mapper.ReceiveMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxFailureScheduler {

    private final RequestBankingManager requestBankingManager;
    private final ReceiveCommandAdaptor receiveCommandAdaptor;
    private final ReceiveQueryAdaptor receiveQueryAdaptor;
    private final ReceiveMapper receiveMapper;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void reDepositForOutboxFailure() {
        receiveQueryAdaptor.findAllPendingReceive().forEach(
                receive -> {
                    Long result = requestBankingManager.requestDeposit(receive);

                    if(result > 0) {
                        receive.setOutboxStatus(OutBoxStatus.COMPLETED);
                        receiveCommandAdaptor.save(receiveMapper.toEntity(receive));
                    }
                }
        );

    }
}
