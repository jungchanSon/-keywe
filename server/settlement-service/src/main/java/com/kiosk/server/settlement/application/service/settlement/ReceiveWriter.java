package com.kiosk.server.settlement.application.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiveWriter {

    private final ApplicationEventPublisher eventPublisher;

}
