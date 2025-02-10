package com.kiosk.server.store.domain;

import java.util.Map;

public interface ImageRepository {

    void insert(MenuImage image);

    MenuImage findById(long menuId);

    Object findImageBytesById(Map<String,Object> params);

    void deleteById(long menuId);
}
