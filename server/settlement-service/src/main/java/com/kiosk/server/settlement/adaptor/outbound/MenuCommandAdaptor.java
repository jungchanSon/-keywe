package com.kiosk.server.settlement.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.MenuEntity;
import com.kiosk.server.infra.rdb.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuCommandAdaptor {

    private final MenuRepository menuRepository;

    public void save(MenuEntity menuEntity) {
        menuRepository.save(menuEntity);
    }
}
