package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;

import java.util.List;

public interface FindAllCategoriesService {

    List<FindAllCategoriesResponse> doService(long userId);
}
