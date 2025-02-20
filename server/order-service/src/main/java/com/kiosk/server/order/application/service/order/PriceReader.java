package com.kiosk.server.order.application.service.order;

import com.kiosk.server.infra.feign.client.MenuClient;
import com.kiosk.server.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class PriceReader {

    private final MenuClient menuClient;

    public Order readPrice(Order order) {

        AtomicReference<Long> totalPrice = new AtomicReference<>(0L);

        order.getOrderMenuList().forEach(
                menu -> {
                    Long menuPrice = requestMenuPrice(menu.getOrderMenuId());

                    totalPrice.updateAndGet(v -> v + menuPrice);

                    menu.setPrice(menuPrice);
                    if (!menu.getOptionList().isEmpty()) {
                        menu.getOptionList().forEach(
                                option -> {
                                    Long optionPrice = requestOptionPrice(menu.getMenuId(), option.getOrderMenuOptionId());

                                    totalPrice.updateAndGet(v -> v + optionPrice);
                                    option.setPrice(optionPrice);
                                }
                        );
                    }
                }
        );
        order.setTotalPrice(totalPrice.get());

        return order;
    }

    public Long requestMenuPrice(Long menuId) {
        return menuClient.requestMenuPrice(menuId).orElseThrow().longValue();
    }

    public Long requestOptionPrice(Long menuId, Long optionId) {
        return menuClient.requestOptionPrice(menuId, optionId).orElseThrow().longValue();
    }
}
