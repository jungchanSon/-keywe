package com.kiosk.order.application.service.order.facade;

import com.kiosk.order.application.port.inbound.OrderUseCase;
import com.kiosk.order.application.service.order.OrderMenuOptionWriter;
import com.kiosk.order.application.service.order.OrderMenuWriter;
import com.kiosk.order.application.service.order.OrderWriter;
import com.kiosk.order.application.service.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade implements OrderUseCase {

    private final OrderWriter orderWriter;
    private final OrderMenuWriter orderMenuWriter;
    private final OrderMenuOptionWriter orderMenuOptionWriter;

    @Override
    @Transactional
    public long commandOrder(OrderVO.save order) {

        long orderId = orderWriter.create(order);

        order.menuList().forEach(
                orderMenu -> {
                    long orderMenuId = orderMenuWriter.create(orderMenu, orderId);
                    saveOptionInMenu(orderMenu, orderId, orderMenuId);
                });

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
