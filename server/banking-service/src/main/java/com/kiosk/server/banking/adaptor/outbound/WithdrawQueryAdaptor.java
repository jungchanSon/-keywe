package com.kiosk.server.banking.adaptor.outbound;

import com.kiosk.server.banking.application.port.outbound.WithdrawQueryPort;
import com.kiosk.server.banking.domain.Withdraw;
import com.kiosk.server.banking.domain.mapper.WithdrawMapper;
import com.kiosk.server.infra.rdb.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WithdrawQueryAdaptor implements WithdrawQueryPort {

    private final WithdrawRepository withdrawRepository;

    private final WithdrawMapper withdrawMapper;

    @Override
    public Withdraw readWithdraw(Long userId) {
        return withdrawMapper.toDomainFromEntity(withdrawRepository.findByUserId(userId).orElseThrow());
    }

    @Override
    public List<Withdraw> searchWithdrawList(Long userId, Long snapShot) {
        return withdrawRepository
                .findDepositEventEntitiesByUserIdAndWithdrawEventIdGreaterThanOrderByCreatedAt(
                        userId,
                        snapShot
                ).stream().map(
                        withdrawMapper::toDomainFromEntity
                ).toList();
    }
}
