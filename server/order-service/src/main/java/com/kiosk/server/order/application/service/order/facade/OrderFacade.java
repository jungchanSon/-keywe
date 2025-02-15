package com.kiosk.server.order.application.service.order.facade;

import com.kiosk.server.order.application.port.inbound.OrderUseCase;
import com.kiosk.server.order.application.service.order.OrderMenuOptionWriter;
import com.kiosk.server.order.application.service.order.OrderMenuWriter;
import com.kiosk.server.order.application.service.order.OrderWriter;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFacade implements OrderUseCase {

    private final OrderWriter orderWriter;
    private final OrderMenuWriter orderMenuWriter;
    private final OrderMenuOptionWriter orderMenuOptionWriter;

    @Override
    @Transactional
    public long commandOrder(OrderVO.save order) {
        log.info("[TRANSACTION-START] 주문하기, shopId={}, phoneNumber={}", order.shopId(), order.phoneNumber());
        long orderId = orderWriter.create(order);

        order.menuList().forEach(
                orderMenu -> {
                    long orderMenuId = orderMenuWriter.create(orderMenu, orderId);
                    saveOptionInMenu(orderMenu, orderId, orderMenuId);
                });
        log.info("[TRANSACTION-END] 주문하기, shopId={}, phoneNumber={}", order.shopId(), order.phoneNumber());
        return orderId;
    }

    private void saveOptionInMenu(OrderVO.OrderMenu orderMenu, long orderId, long orderMenuId) {
        orderMenu.optionList().forEach(
                option -> {
                    orderMenuOptionWriter.create(option, orderId, orderMenuId);
                }
        );
    }
}
