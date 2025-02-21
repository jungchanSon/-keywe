//package com.kiosk.server.infra.kafka.consumer;
//
//import com.kiosk.server.infra.kafka.event.OrderEvent;
//import com.kiosk.server.infra.kafka.event.mapper.OrderEventMapper;
//import com.kiosk.server.settlement.application.port.inbound.SettlementManager;
//import com.kiosk.server.util.KafkaEventMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OrderEventConsumer {
//
//    private final SettlementManager settlementManager;
//    private final OrderEventMapper orderEventMapper;
//
//    @KafkaListener(topics = "create-order", groupId = "settlement-service")
//    public void consumeOrderCreatedEvent(String message) {
//        OrderEvent.CreatedEvent orderCreatedEvent = KafkaEventMapper.toObject(message, OrderEvent.CreatedEvent.class);
//
//        log.info("[정산] 정산 시작: shopId={}, amount={}", orderCreatedEvent.shopId(), orderCreatedEvent.totalPrice());
//
//        settlementManager.settle(orderEventMapper.toDomain(orderCreatedEvent));
//
//        log.info("[정산] 정산 완료: shopId={}, amount={}", orderCreatedEvent.shopId(), orderCreatedEvent.totalPrice());
//    }
//}
