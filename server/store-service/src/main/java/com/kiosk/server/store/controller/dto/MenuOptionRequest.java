package com.kiosk.server.store.controller.dto;

public record MenuOptionRequest(String optionType, String optionName, String optionValue, int optionPrice,
                                Long optionId) {
}
