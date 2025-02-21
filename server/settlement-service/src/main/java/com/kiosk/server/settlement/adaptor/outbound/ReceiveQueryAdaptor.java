package com.kiosk.server.settlement.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import com.kiosk.server.infra.rdb.repository.ReceiveRepository;
import com.kiosk.server.settlement.domain.Receive;
import com.kiosk.server.settlement.domain.mapper.ReceiveMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiveQueryAdaptor {

    private final ReceiveRepository receiveRepository;
    private final ReceiveMapper receiveMapper;

    public List<Receive> findAllPendingReceive() {
        return receiveRepository.findAllByOutboxStatus(OutBoxStatus.PENDING)
                .stream()
                .map(
                        receiveMapper::toDomain
                ).toList();
    }

    public Receive findById(Long id) {
        return receiveMapper.toDomain(receiveRepository.findById(id).orElseThrow());
    }
}
