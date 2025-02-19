package com.kiosk.server.banking.adaptor.inbound.api;

import com.kiosk.server.banking.adaptor.inbound.api.dto.BalanceRequestDTO;
import com.kiosk.server.banking.adaptor.inbound.api.mapper.BalanceDTOMapper;
import com.kiosk.server.banking.application.port.inbound.BalanceHistoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceApi {

    private final BalanceHistoryCommand balanceHistoryCommand;
    private final BalanceDTOMapper balanceDTOMapper;

    @PostMapping
    public Long recordBalance(@RequestBody BalanceRequestDTO request) {
        return balanceHistoryCommand.recordTransaction(balanceDTOMapper.toVO(request));
    }
}
