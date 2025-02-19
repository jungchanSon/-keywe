package com.kiosk.server.settlement.application.port.inbound;

import com.kiosk.server.settlement.domain.Receive;

public interface SettlementManager {
    void settle(Receive receive);
}
