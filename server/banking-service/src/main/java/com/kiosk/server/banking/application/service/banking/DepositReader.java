package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.outbound.DepositQueryPort;
import com.kiosk.server.banking.domain.Deposit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositReader  {

    private final DepositQueryPort depositQueryPort;

    public List<Deposit> searchDepositList(Long userId, Long snapShot) {
        return depositQueryPort.searchDepositList(userId, snapShot);
    }

}
