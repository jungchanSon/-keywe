package com.kiosk.server.store.util;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.controller.dto.OptionResponse;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.domain.StoreMenuOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OptionServiceUtil {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository optionRepository;

    // 메뉴와 옵션 존재 여부 검증
    public void validateMenuAndOption(long userId, long menuId, Long optionId) {
        StoreMenu menu = menuRepository.findById(userId, menuId);
        if (menu == null) {
            throw new EntityNotFoundException("Menu not found");
        }

        if (optionId != null) {
            StoreMenuOption existingOption = optionRepository.findOptionById(menuId, optionId);
            if (existingOption == null) {
                throw new EntityNotFoundException("Option not found");
            }
        }
    }

    // 옵션 리스트 저장하고 옵션 그룹 응답 생성
    public List<OptionGroupResponse> saveOptionsAndGetResponse(List<StoreMenuOption> options) {
        if (options == null || options.isEmpty()) {
            return new ArrayList<>();
        }
        optionRepository.insert(options);
        return createOptionGroupResponse(options);
    }

    // 단일 옵션 추가 시 사용
    public List<OptionGroupResponse> addOptionAndGetResponse(StoreMenuOption option) {
        optionRepository.insert(List.of(option));
        return getUpdatedOptionGroups(option.getMenuId());
    }


    // 최신 옵션 목록을 조회하고 옵션 그룹 응답을 생성
    public List<OptionGroupResponse> getUpdatedOptionGroups(long menuId) {
        List<StoreMenuOption> updatedOptions = optionRepository.findDetailOptionsById(menuId);
        return createOptionGroupResponse(updatedOptions);
    }

    // 옵션 그룹 응답을 생성하는 메서드
    private List<OptionGroupResponse> createOptionGroupResponse(List<StoreMenuOption> options) {
        List<OptionGroupResponse> optionGroups = new ArrayList<>();
        Map<Long, OptionGroupResponse> groupedOptions = new HashMap<>();

        for (StoreMenuOption opt : options) {
            // 옵션 그룹이 없으면 새로 생성
            if (!groupedOptions.containsKey(opt.getOptionGroupId())) {
                groupedOptions.put(opt.getOptionGroupId(),
                        new OptionGroupResponse(opt.getOptionGroupId(), opt.getOptionName(), opt.getOptionType(), new ArrayList<>()));
            }
            // 해당 그룹의 옵션 리스트에 옵션 추가
            groupedOptions.get(opt.getOptionGroupId()).options().add(new OptionResponse(opt.getOptionId(), opt.getOptionValue()));
        }

        optionGroups.addAll(groupedOptions.values());
        return optionGroups;
    }
}
