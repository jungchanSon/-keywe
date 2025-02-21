package com.kiosk.server.settlement.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.MenuOptionEntity;
import com.kiosk.server.infra.rdb.repository.MenuOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptionCommandAdaptor {

    private final MenuOptionRepository menuOptionRepository;

    public void save(MenuOptionEntity menuOptionEntity) {
        menuOptionRepository.save(menuOptionEntity);
    }
}
