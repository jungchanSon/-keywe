package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.DepositQueryPort;
import com.kiosk.server.banking.domain.Deposit;
import com.kiosk.server.banking.domain.mapper.DepositMapper;
import com.kiosk.server.infra.rdb.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositQueryAdaptor implements DepositQueryPort {

    private final DepositRepository depositRepository;

    private final DepositMapper depositMapper;

    @Override
    public Deposit readDeposit(Long userId) {
        return depositMapper.toDomainEntity(depositRepository.getByUserId(userId).orElseThrow());
    }

    @Override
    public List<Deposit> searchDepositList(Long userId, Long snapShot) {
        return depositRepository
                .findDepositEventEntitiesByUserIdAndDepositEventIdGreaterThanOrderByLocalDateTimeAsc(
                        userId,
                        snapShot
                ).stream().map(
                        depositMapper::toDomainEntity
                ).collect(Collectors.toList());
    }
}
