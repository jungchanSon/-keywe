package com.kiosk.server.image.repository;

import com.kiosk.server.image.domain.Image;

import java.util.Map;

public interface ImageRepository {
    void insert(Image image);

    Image findByTargetId(long targetId);

    Object findImageBytesById(Map<String, Object> params);

    void deleteById(long targetId);
}
