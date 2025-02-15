package com.kiosk.server.banking.application.port.inbound;

import com.kiosk.server.banking.application.service.banking.vo.AccountVO;

public interface AccountCommand {
    long create(AccountVO.Save account);
}
