package com.kiosk.server.order.application.service.order.facade;

import com.kiosk.server.infra.kafka.event.OrderCompensatingEvent;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.infra.kafka.event.mapper.OrderEventMapper;
import com.kiosk.server.order.application.port.inbound.OrderUseCase;
import com.kiosk.server.order.application.service.order.*;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacade implements OrderUseCase {

    private final OrderWriter orderWriter;
    private final OrderMenuWriter orderMenuWriter;
    private final OrderMenuOptionWriter orderMenuOptionWriter;
    private final BankingWriter bankingWriter;
    private final UserReader userReader;

    private final ApplicationEventPublisher eventPublisher;
    private final OrderEventMapper orderEventMapper;

    @Override
    @Transactional
    public long commandOrder(OrderVO.save order) {
        log.info("[TRANSACTION-START] 주문하기, shopId={}, phoneNumber={}", order.shopId(), order.phoneNumber());
        Long userId = requestUserId(order.profileId());
        Long withdraw = bankingWriter.withdraw(userId, order.totalPrice());
        if (withdraw > 0) {
            publishOrderCompensatingEvent(userId, order.totalPrice());
        }

        long orderRecordId = createOrderRecord(order);

        publishOrderCreatedEvent(order, orderRecordId, userId);

        log.info("[TRANSACTION-END] 주문하기, shopId={}, phoneNumber={}", order.shopId(), order.phoneNumber());

        return orderRecordId;
    }

    private void publishOrderCompensatingEvent(Long userId, long totalPrice) {
        OrderCompensatingEvent.CompensatingEvent orderCompensatingEvent = orderEventMapper.toOrderCompensatingEvent(userId, totalPrice);
        eventPublisher.publishEvent(orderCompensatingEvent);
    }

    private void publishOrderCreatedEvent(OrderVO.save order, long orderRecordId, Long customerId) {
        OrderEvent.OrderCreatedEvent orderCreatedEvent = orderEventMapper.toOrderCreatedEvent(order, orderRecordId, customerId);
        eventPublisher.publishEvent(orderCreatedEvent);
    }

    private long createOrderRecord(OrderVO.save order) {
        long orderId = orderWriter.create(order);
        order.menuList().forEach(
                orderMenu -> {
                    long orderMenuId = orderMenuWriter.create(orderMenu, orderId);
                    saveOptionInMenu(orderMenu, orderId, orderMenuId);
                });

        return orderId;
    }

    private Long requestUserId(long profileId) {
        Long userId = userReader.requestUserId(profileId);
        log.info("[Order][주문] userId 조회. userId={}", userId);
        return userReader.requestUserId(profileId);
    }

    private void saveOptionInMenu(OrderVO.OrderMenu orderMenu, long orderId, long orderMenuId) {
        orderMenu.optionList().forEach(
                option -> {
                    orderMenuOptionWriter.create(option, orderId, orderMenuId);
                }
        );
    }
}
