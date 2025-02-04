package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenuOption;
import com.kiosk.server.store.service.AddOptionService;
import com.kiosk.server.store.util.OptionServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddOptionServiceImpl implements AddOptionService {

    private final MenuRepository menuRepository;
    private final OptionServiceUtil optionServiceUtil;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CreateMenuResponse doService(long userId, long menuId, MenuOptionRequest request) {
        if (request == null) {
            throw new EntityNotFoundException("No option data provided");
        }

        optionServiceUtil.validateMenuAndOption(userId, menuId, null);

        // 기존 옵션 그룹 확인
        Long optionGroupId = determineOptionGroupId(menuId, request.optionGroupId());

        // 옵션 객체 생성
        StoreMenuOption option = StoreMenuOption.create(
                menuId,
                request.optionType(),
                request.name(),
                request.value(),
                optionGroupId
        );

        // 중복 검증
        validateOptionUniqueness(option);

        // 옵션 저장 및 응답 생성
        List<OptionGroupResponse> optionGroups = optionServiceUtil.addOptionAndGetResponse(option);

        return new CreateMenuResponse(menuId, optionGroups);
    }

    private Long determineOptionGroupId(long menuId, Long requestedGroupId) {
        if (requestedGroupId == null || requestedGroupId == -1) {
            return IdUtil.create();
        }

        // 요청된 그룹 ID가 존재하는지 확인
        if (!menuRepository.existsOptionGroupId(menuId, requestedGroupId)) {
            throw new EntityNotFoundException("Option group not found: " + requestedGroupId);
        }

        return requestedGroupId;
    }

    private void validateOptionUniqueness(StoreMenuOption newOption) {
        if (menuRepository.existsOptionById(newOption.getOptionId())) {
            throw new DuplicateKeyException("Duplicate option id");
        }
    }
}