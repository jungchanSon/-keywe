package com.kiosk.server.image.service;

public interface DeleteImageService {

    void doService(long userId, long targetId, String type);
}
