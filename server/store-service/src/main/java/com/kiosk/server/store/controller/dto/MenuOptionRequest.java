package com.kiosk.server.store.controller.dto;

public record MenuOptionRequest(String optionType, String name, String value, Long optionGroupId) {
}
