package com.kiosk.server.store.domain;

import java.util.Map;

public interface MenuImageRepository {

    void insert(MenuImage image);

    MenuImage findByMenuId(long menuId);

    Object findImageBytesById(Map<String,Object> params);

    void deleteById(long menuId);
}
