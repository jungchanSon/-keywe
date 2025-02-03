package com.kiosk.server.store.service.impl;

import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.service.FindAllCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllCategoriesServiceImpl implements FindAllCategoriesService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<FindAllCategoriesResponse> doService(long userId) {
        return categoryRepository.findAllCategoriesByUserId(userId);
    }
}
