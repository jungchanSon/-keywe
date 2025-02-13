package com.kiosk.order.adaptor.inbound.api;

import com.kiosk.order.adaptor.inbound.api.dto.OrderRequestDTO;
import com.kiosk.order.adaptor.inbound.api.dto.OrderResponseDTO;
import com.kiosk.order.adaptor.inbound.api.mapper.OrderDTOMapper;
import com.kiosk.order.application.port.inbound.OrderCommand;
import com.kiosk.order.application.port.inbound.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderApi {

    private final OrderUseCase orderUseCase;

    private final OrderDTOMapper orderDTOMapper;

    @PostMapping
    public ResponseEntity<OrderResponseDTO.Response> createOrder(@RequestHeader("userId") long shopId, @RequestBody OrderRequestDTO.Order order) {
        long orderId = orderUseCase.commandOrder(orderDTOMapper.toVO(order, shopId));

        return ResponseEntity.ok().body(
                OrderResponseDTO.Response.builder()
                        .orderId(String.valueOf(orderId))
                        .build()
        );
    }

}
