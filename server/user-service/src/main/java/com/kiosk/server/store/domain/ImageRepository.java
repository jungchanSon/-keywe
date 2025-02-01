package com.kiosk.server.store.domain;

public interface ImageRepository {

    void insertImage(Images image);

    Images findImageById(int imageId);

    void deleteImageById(int imageId);
}
