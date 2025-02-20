package com.kiosk.server.settlement.application.port.outbound;

import com.kiosk.server.settlement.domain.Receive;

public interface ReceiveCommandPort {

    Long createReceive(Receive receive);
}
