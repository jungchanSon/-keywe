package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.domain.StoreMenuOption;
import com.kiosk.server.store.service.AddOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddOptionServiceImpl implements AddOptionService {

    private final MenuOptionRepository optionRepository;
    private final OptionServiceImpl optionService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CreateMenuResponse doService(long userId, long menuId, MenuOptionRequest request) {
        if (request == null) {
            throw new EntityNotFoundException("No option data provided");
        }

        StoreMenu menu = optionService.validateMenuAndOption(userId, menuId, null);

        // 기존 옵션 그룹 확인
        Long optionGroupId = determineOptionGroupId(menuId, request.optionGroupId());

        // 옵션 객체 생성
        StoreMenuOption option = StoreMenuOption.create(
                menuId,
                request.optionType(),
                request.optionName(),
                request.optionValue(),
                request.optionPrice(),
                optionGroupId
        );

        // 중복 검증
        validateOptionUniqueness(option);

        // 옵션 저장 및 응답 생성
        List<OptionGroupResponse> optionGroups = optionService.addOptionAndGetResponse(option);

        return new CreateMenuResponse(menuId, menu.getMenuName(), menu.getMenuPrice(), optionGroups);
    }

    private Long determineOptionGroupId(long menuId, Long requestedGroupId) {
        if (requestedGroupId == null || requestedGroupId == -1) {
            return IdUtil.create();
        }

        // 요청된 그룹 ID가 존재하는지 확인
        if (!optionRepository.existsOptionGroupById(menuId, requestedGroupId)) {
            throw new EntityNotFoundException("Option group not found: " + requestedGroupId);
        }

        return requestedGroupId;
    }

    private void validateOptionUniqueness(StoreMenuOption newOption) {
        if (optionRepository.existsOptionById(newOption.getOptionId())) {
            throw new DuplicateKeyException("Duplicate option id");
        }
    }
}
