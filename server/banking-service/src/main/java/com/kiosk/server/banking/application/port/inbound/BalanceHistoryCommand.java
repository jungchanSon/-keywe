package com.kiosk.server.banking.application.port.inbound;

import com.kiosk.server.banking.application.service.banking.vo.BalanceHistoryVO;

public interface BalanceHistoryCommand {

    Long recordTransaction(BalanceHistoryVO.Save record);
}
