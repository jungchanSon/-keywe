package com.kiosk.server.banking.application.port.inbound;

import com.kiosk.server.banking.application.service.banking.vo.DepositVO;

public interface DepositCommand {

    Long deposit(DepositVO.Save deposit);
}
