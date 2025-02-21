package com.kiosk.server.settlement.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Menu {

    Long menuId;
    Long orderMenuId;
    Long orderId;
    String name;
    Long price;
    Long count;
    List<MenuOption> menuOptionList;
}
