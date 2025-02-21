package com.kiosk.server.store.controller.dto;

import java.util.List;

public record MenuOptionData(String optionType, String optionName, int optionPrice, List<String> optionValue) {
}
