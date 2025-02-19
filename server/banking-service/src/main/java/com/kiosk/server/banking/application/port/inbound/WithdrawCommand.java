package com.kiosk.server.banking.application.port.inbound;

import com.kiosk.server.banking.application.service.banking.vo.WithdrawVO;

public interface WithdrawCommand {
    Long withdraw(WithdrawVO.Save withDraw);
}