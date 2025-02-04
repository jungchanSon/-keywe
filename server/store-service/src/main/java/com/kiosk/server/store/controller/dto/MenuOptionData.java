package com.kiosk.server.store.controller.dto;

import java.util.List;

public record MenuOptionData(String optionType, String name, List<String> value) {
}
