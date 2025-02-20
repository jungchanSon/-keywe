package com.kiosk.server.order.application.service.order;

import com.kiosk.server.order.application.port.outbound.OutboxQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxOrderReader {

    private final OutboxQueryPort outboxQueryPort;


}
