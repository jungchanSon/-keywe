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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddOptionServiceImpl implements AddOptionService {

    private final MenuOptionRepository optionRepository;
    private final OptionServiceImpl optionService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CreateMenuResponse doService(long userId, long menuId, MenuOptionRequest request) {
        log.info("AddOptionService: userId={}, menuId={}, request={}", userId, menuId, request);

        if (request == null) {
            log.warn("옵션 추가 실패 - 요청된 옵션 정보 없음");
            throw new EntityNotFoundException("옵션 정보가 없습니다. 옵션 정보를 입력해 주세요.");
        }

        StoreMenu menu = optionService.validateMenuAndOption(userId, menuId, null);

        // 기존 옵션 그룹 확인
        Long optionId = determineOptionId(menuId, request.optionId());

        // 옵션 객체 생성
        StoreMenuOption option = StoreMenuOption.create(
                menuId,
                request.optionType(),
                request.optionName(),
                request.optionValue(),
                request.optionPrice(),
                optionId
        );

        // 중복 검증
        validateOptionUniqueness(option);

        // 옵션 저장 및 응답 생성
        List<OptionGroupResponse> optionGroups = optionService.addOptionAndGetResponse(option);
        log.info("옵션 추가 성공 - menuId={}, optionId={}, optionName={}", menuId, option.getOptionId(), option.getOptionName());

        return new CreateMenuResponse(menuId, menu.getMenuName(), menu.getMenuPrice(), optionGroups);
    }

    private Long determineOptionId(long menuId, Long requestedId) {
        if (requestedId == null || requestedId == -1) {
            return IdUtil.create();
        }

        // 요청된 그룹 ID가 존재하는지 확인
        if (!optionRepository.existsOptionGroupById(menuId, requestedId)) {
            log.warn("옵션 그룹 없음 - menuId={}, requestedOptionId={}", menuId, requestedId);
            throw new EntityNotFoundException("해당 옵션 그룹을 찾을 수 없습니다. 올바른 옵션 그룹을 선택해 주세요.");
        }

        return requestedId;
    }

    private void validateOptionUniqueness(StoreMenuOption newOption) {
        if (optionRepository.existsOptionById(newOption.getOptionId())) {
            log.warn("중복된 옵션 추가 시도 - optionId={}, optionName={}", newOption.getOptionId(), newOption.getOptionName());
            throw new DuplicateKeyException("이미 등록된 옵션입니다. 다른 옵션을 사용해 주세요.");
        }
    }
}
