package com.kiosk.server.banking.adaptor.inbound.api;


import com.kiosk.server.banking.adaptor.inbound.api.dto.DepositRequestDTO;
import com.kiosk.server.banking.adaptor.inbound.api.mapper.DepositDTOMapper;
import com.kiosk.server.banking.application.port.inbound.DepositCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
@RequiredArgsConstructor
public class DepositApi {

    private final DepositCommand depositCommand;
    private final DepositDTOMapper depositDTOMapper;

    @PostMapping
    public Long deposit(@RequestBody DepositRequestDTO.Deposit request) {
        return depositCommand.deposit(depositDTOMapper.toVO(request));
    }
}
