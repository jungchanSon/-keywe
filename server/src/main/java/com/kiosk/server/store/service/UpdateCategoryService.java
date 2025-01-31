package com.kiosk.server.store.service;

public interface UpdateCategoryService {

    void doService(long userId, long categoryId, String newCategoryName);
}
