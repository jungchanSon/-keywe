package com.kiosk.server.settlement.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuOption {
    Long menuOptionId;
    Long orderOptionId;
    Long menuId;
    String name;
    Long price;
    Long count;
}
