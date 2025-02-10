package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.service.UpdateOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateOptionServiceImpl implements UpdateOptionService {

    private final MenuOptionRepository optionRepository;
    private final OptionServiceImpl optionService;

    @Override
    public CreateMenuResponse doService(long userId, long menuId, long optionId, MenuOptionRequest request) {
        if (request == null || optionId < 0) {
            throw new BadRequestException("Option ID is required for update");
        }

        optionService.validateMenuAndOption(userId, menuId, optionId);

        Map<String, Object> updateMap = getUpdateMap(optionId, request);

        optionRepository.update(updateMap);

        // 최신 옵션 목록 조회 후 응답 반환
        List<OptionGroupResponse> optionGroups = optionService.getUpdatedOptionGroups(menuId);

        return new CreateMenuResponse(menuId, optionGroups);
    }

    private static Map<String, Object> getUpdateMap(long optionId, MenuOptionRequest request) {
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("optionId", optionId);
        updateMap.put("optionType", request.optionType());
        updateMap.put("optionName", request.name());
        updateMap.put("optionValue", request.value());
        updateMap.put("optionGroupId", request.optionGroupId());

        // optionId가 제대로 포함되어 있는지 확인
        if (!updateMap.containsKey("optionId") || updateMap.get("optionId") == null) {
            throw new EntityNotFoundException("optionId is missing in update map");
        }
        return updateMap;
    }
}
