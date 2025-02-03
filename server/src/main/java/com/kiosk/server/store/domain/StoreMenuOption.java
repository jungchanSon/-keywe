package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.store.controller.dto.MenuOptionData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenuOption {
    private long optionId;
    private long optionGroupId;  // 동일 옵션 그룹에 속하는 옵션들은 같은 값을 가짐
    private long menuId;
    private String optionType;
    private String optionName;   // 옵션 그룹명
    private String optionValue;  // 개별 옵션 값
    private LocalDateTime createdAt;

    private StoreMenuOption(long optionId, long menuId, String optionType, String optionName, String optionValue, long optionGroupId) {
        this.optionId = optionId;
        this.optionGroupId = optionGroupId;
        this.menuId = menuId;
        this.optionType = optionType;
        this.optionName = validateInput(optionName, "optionName");
        this.optionValue = validateInput(optionValue, "optionValue");
        this.createdAt = LocalDateTime.now();
    }

    public static StoreMenuOption create(long menuId, String optionType, String optionName, String optionValue, Long optionGroupId) {
        long newOptionId = IdUtil.create();
        return new StoreMenuOption(newOptionId, menuId, optionType, optionName, optionValue, optionGroupId);
    }

    public static List<StoreMenuOption> createFromList(List<MenuOptionData> dtoList, long menuId) {
        List<StoreMenuOption> list = new ArrayList<>();
        if (dtoList != null) {
            for (MenuOptionData dto : dtoList) {
                List<String> values = dto.value();
                if (values == null || values.isEmpty()) {
                    throw new EntityNotFoundException("No MenuOptionData for '" + dto.name() + "'");
                }
                // 새 그룹 ID 생성
                long groupId = IdUtil.create();

                for (String optionValue : values) {
                    StoreMenuOption option = StoreMenuOption.create(menuId, dto.optionType(), dto.name(), optionValue, groupId);
                    list.add(option);
                }
            }
        }
        return list;
    }

    private static String validateInput(String input, String fieldName) {
        if (!StringUtils.hasLength(input)) {
            throw new EntityNotFoundException(fieldName + "Name cannot be empty");
        }
        return input;
    }
}
