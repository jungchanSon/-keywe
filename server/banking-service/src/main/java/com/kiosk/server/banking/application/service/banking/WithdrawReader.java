package com.kiosk.server.banking.application.service.banking;

import com.kiosk.server.banking.application.port.outbound.WithdrawQueryPort;
import com.kiosk.server.banking.domain.Withdraw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawReader {

    private final WithdrawQueryPort withdrawQueryPort;

    public List<Withdraw> searchDepositList(Long userId, Long snapShot) {
        return withdrawQueryPort.searchWithdrawList(userId, snapShot);
    }

}
