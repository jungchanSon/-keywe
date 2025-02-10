package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.service.UpdateOptionService;
import com.kiosk.server.store.util.OptionServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateOptionServiceImpl implements UpdateOptionService {

    private final MenuOptionRepository optionRepository;
    private final OptionServiceUtil optionServiceUtil;

    @Override
    public CreateMenuResponse doService(long userId, long menuId, long optionId, MenuOptionRequest request) {
        if (request == null || optionId < 0) {
            throw new BadRequestException("Option ID is required for update");
        }

        optionServiceUtil.validateMenuAndOption(userId, menuId, optionId);

        // 옵션 업데이트 실행
        Map<String, Object> updateMap = request.toMap(optionId);

        // optionId가 제대로 포함되어 있는지 확인
        if (!updateMap.containsKey("optionId") || updateMap.get("optionId") == null) {
            throw new EntityNotFoundException("optionId is missing in update map");
        }

        optionRepository.update(updateMap);

        // 최신 옵션 목록 조회 후 응답 반환
        List<OptionGroupResponse> optionGroups = optionServiceUtil.getUpdatedOptionGroups(menuId);

        return new CreateMenuResponse(menuId, optionGroups);
    }
}