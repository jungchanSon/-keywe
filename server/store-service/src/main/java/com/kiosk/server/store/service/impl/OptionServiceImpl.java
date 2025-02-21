package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.controller.dto.OptionResponse;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.domain.StoreMenuOption;
import com.kiosk.server.store.service.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository optionRepository;

    // 메뉴와 옵션 존재 여부 검증
    public StoreMenu validateMenuAndOption(long userId, long menuId, Long optionId) {
        log.info("OptionService: validateMenuAndOption - userId={}, menuId={}, optionId={}", userId, menuId, optionId);

        StoreMenu menu = menuRepository.findById(userId, menuId);
        if (menu == null) {
            log.warn("메뉴 조회 실패 - userId={}, menuId={}", userId, menuId);
            throw new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다. 입력하신 메뉴 정보를 다시 확인해 주세요.");
        }

        if (optionId != null) {
            StoreMenuOption existingOption = optionRepository.findByOptionId(menuId, optionId);
            if (existingOption == null) {
                log.warn("옵션 조회 실패 - menuId={}, optionId={}", menuId, optionId);
                throw new EntityNotFoundException("해당 옵션을 찾을 수 없습니다. 입력하신 옵션 정보를 다시 확인해 주세요.");
            }
        }

        return menu;
    }

    // 옵션 리스트 저장하고 옵션 그룹 응답 생성
    public List<OptionGroupResponse> saveOptionsAndGetResponse(List<StoreMenuOption> options) {
        log.info("OptionService: saveOptionsAndGetResponse - 옵션 개수={}", (options != null ? options.size() : 0));

        if (options == null || options.isEmpty()) {
            return new ArrayList<>();
        }
        optionRepository.insert(options);
        return createOptionGroupResponse(options);
    }

    // 단일 옵션 추가 시 사용
    public List<OptionGroupResponse> addOptionAndGetResponse(StoreMenuOption option) {
        log.info("OptionService: addOptionAndGetResponse - menuId={}, optionId={}", option.getMenuId(), option.getOptionId());

        optionRepository.insert(List.of(option));
        return getUpdatedOptionGroups(option.getMenuId());
    }


    // 최신 옵션 목록을 조회하고 옵션 그룹 응답을 생성
    public List<OptionGroupResponse> getUpdatedOptionGroups(long menuId) {
        log.info("OptionService: getUpdatedOptionGroups - menuId={}", menuId);

        List<StoreMenuOption> updatedOptions = optionRepository.findOptionsByMenuId(menuId);
        return createOptionGroupResponse(updatedOptions);
    }

    // 옵션 그룹 응답을 생성하는 메서드
    private List<OptionGroupResponse> createOptionGroupResponse(List<StoreMenuOption> options) {
        log.info("OptionService: createOptionGroupResponse - 옵션 개수={}", options.size());

        Map<Long, OptionGroupResponse> groupedOptions = new HashMap<>();

        for (StoreMenuOption option : options) {
            groupedOptions.computeIfAbsent(
                    option.getOptionGroupId(),
                    groupId -> new OptionGroupResponse(
                            option.getOptionGroupId(),
                            option.getOptionName(),
                            option.getOptionType(),
                            option.getOptionPrice(),
                            new ArrayList<>()
                    )
            ).optionsValueGroup().add(new OptionResponse(option.getOptionId(), option.getOptionValue()));
        }

        log.info("옵션 그룹 응답 생성 완료 - 그룹 개수={}", groupedOptions.size());
        return new ArrayList<>(groupedOptions.values());
    }
}
