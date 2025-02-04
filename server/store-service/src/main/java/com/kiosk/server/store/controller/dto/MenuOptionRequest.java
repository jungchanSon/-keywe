package com.kiosk.server.store.controller.dto;

import java.util.HashMap;
import java.util.Map;

public record MenuOptionRequest(String optionType, String name, String value, Long optionGroupId) {

    public Map<String, Object> toMap(long optionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("optionId", optionId);
        map.put("optionType", optionType);
        map.put("optionName", name);
        map.put("optionValue", value);
        map.put("optionGroupId", optionGroupId);

        return map;
    }
}
