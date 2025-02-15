package com.kiosk.server.order.adaptor.inbound.api.dto;

import lombok.Builder;

@Builder
public class OrderResponseDTO {

    @Builder
    public record Response (
        String orderId
    ) { }
}
