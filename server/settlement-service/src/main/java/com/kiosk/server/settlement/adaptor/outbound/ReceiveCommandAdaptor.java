package com.kiosk.server.settlement.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.ReceiveEntity;
import com.kiosk.server.infra.rdb.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiveCommandAdaptor {

    private final ReceiveRepository receiveRepository;

    public void save(ReceiveEntity receiveEntity) {
        receiveRepository.save(receiveEntity);
    }

}
