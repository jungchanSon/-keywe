package com.kiosk.server.store.controller.dto;

import java.util.List;

public record OptionGroupResponse (long optionId, String optionName, String optionType, int optionPrice, List<OptionResponse> optionsValueGroup) {
}
