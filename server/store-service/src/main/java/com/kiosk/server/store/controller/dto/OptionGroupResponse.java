package com.kiosk.server.store.controller.dto;

import java.util.List;

public record OptionGroupResponse(long optionGroupId, String optionName, String optionType,
                                  List<OptionResponse> options) {
}
