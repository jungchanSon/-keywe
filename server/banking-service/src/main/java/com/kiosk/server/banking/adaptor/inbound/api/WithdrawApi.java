package com.kiosk.server.banking.adaptor.inbound.api;

import com.kiosk.server.banking.adaptor.inbound.api.dto.WithdrawRequestDTO;
import com.kiosk.server.banking.adaptor.inbound.api.mapper.WithdrawDTOMapper;
import com.kiosk.server.banking.application.port.inbound.WithdrawCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/withdraw")
@RequiredArgsConstructor
public class WithdrawApi {

    private final WithdrawCommand withdrawCommand;
    private final WithdrawDTOMapper withdrawMapper;

    @PostMapping
    public Long withdraw(@RequestBody WithdrawRequestDTO.Withdraw withdraw) {
        return withdrawCommand.withdraw(withdrawMapper.toVO(withdraw));
    }
}
