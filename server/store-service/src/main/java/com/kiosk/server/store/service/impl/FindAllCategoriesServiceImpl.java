package com.kiosk.server.store.service.impl;

import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.service.FindAllCategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindAllCategoriesServiceImpl implements FindAllCategoriesService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<FindAllCategoriesResponse> doService(long userId) {
        log.info("FindAllCategoriesService: userId={}", userId);

        return categoryRepository.findAllCategoriesByUserId(userId);
    }
}
