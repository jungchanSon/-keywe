package com.kiosk.server.settlement.application.service.settlement;

import com.kiosk.server.infra.feign.client.MenuClient;
import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import com.kiosk.server.settlement.adaptor.outbound.MenuCommandAdaptor;
import com.kiosk.server.settlement.adaptor.outbound.OptionCommandAdaptor;
import com.kiosk.server.settlement.adaptor.outbound.ReceiveCommandAdaptor;
import com.kiosk.server.settlement.adaptor.outbound.ReceiveQueryAdaptor;
import com.kiosk.server.settlement.domain.Receive;
import com.kiosk.server.settlement.domain.mapper.ReceiveMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@RequiredArgsConstructor
public class OutboxManager {

    private final ReceiveMapper receiveMapper;
    private final ReceiveCommandAdaptor receiveCommandAdaptor;
    private final MenuCommandAdaptor menuCommandAdaptor;
    private final OptionCommandAdaptor optionCommandAdaptor;
    private final ReceiveQueryAdaptor receiveQueryAdaptor;

    private final MenuClient menuClient;

    @TransactionalEventListener( phase = TransactionPhase.BEFORE_COMMIT )
    public void outboxReceive(Receive receive) {
        receive.setOutboxStatus(OutBoxStatus.PENDING);

        receive.getMenuList().forEach(
            menu -> {
                menuCommandAdaptor.save(receiveMapper.toEntity(menu));
                if(!menu.getMenuOptionList().isEmpty()){
                    menu.getMenuOptionList().forEach(
                        option -> {
                            optionCommandAdaptor.save(receiveMapper.toEntity(option));
                        }
                    );
                }
            }
        );

        receiveCommandAdaptor.save(receiveMapper.toEntity(receive));
    }

    @Async
    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void outboxComplete(Receive receive) {
        Receive complete = receiveQueryAdaptor.findById(receive.getReceiveId());
        complete.setOutboxStatus(OutBoxStatus.COMPLETED);
        receiveCommandAdaptor.save(receiveMapper.toEntity(complete));
    }

    public Long requestMenuPrice(Long menuId) {
        return menuClient.requestMenuPrice(menuId).orElseThrow().longValue();
    }

    public Long requestOptionPrice(Long menuId, Long optionId) {
        return menuClient.requestOptionPrice(menuId, optionId).orElseThrow().longValue();
    }
}
