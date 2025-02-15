package com.kiosk.banking.application.port.inbound;

import com.kiosk.banking.application.service.banking.vo.AccountVO;

public interface AccountCommand {
    long create(AccountVO.Save account);
}
