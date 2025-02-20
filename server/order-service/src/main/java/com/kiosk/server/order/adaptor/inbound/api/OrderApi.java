package com.kiosk.server.order.adaptor.inbound.api;

import com.kiosk.server.order.adaptor.inbound.api.dto.OrderRequestDTO;
import com.kiosk.server.order.adaptor.inbound.api.dto.OrderResponseDTO;
import com.kiosk.server.order.adaptor.inbound.api.mapper.OrderDTOMapper;
import com.kiosk.server.order.application.port.inbound.OrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderUseCase orderUseCase;
    private final OrderDTOMapper orderDTOMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDTO.Response> createOrder(@RequestHeader("userId") long shopId, @RequestBody OrderRequestDTO.Order order) {
        log.info("[주문] 주문 요청 userId={}, order={}", shopId, order);
        long orderId = orderUseCase.commandOrder(orderDTOMapper.toVO(order, shopId));

        return ResponseEntity.ok().body(
                OrderResponseDTO.Response.builder()
                        .orderId(String.valueOf(orderId))
                        .build()
        );
    }

}
