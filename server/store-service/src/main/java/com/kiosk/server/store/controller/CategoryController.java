package com.kiosk.server.store.controller;

import com.kiosk.server.store.controller.dto.CategoryRequest;
import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;
import com.kiosk.server.store.service.CreateCategoryService;
import com.kiosk.server.store.service.DeleteCategoryService;
import com.kiosk.server.store.service.FindAllCategoriesService;
import com.kiosk.server.store.service.UpdateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryService createCategoryService;
    private final DeleteCategoryService deleteCategoryService;
    private final UpdateCategoryService updateCategoryService;
    private final FindAllCategoriesService findAllCategoriesService;

    @PostMapping
    public ResponseEntity<String> insertCategory(@RequestHeader("userId") Long userId, @RequestBody CategoryRequest request) {
        long categoryId = createCategoryService.doService(userId, request.categoryName());
        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@RequestHeader("userId") Long userId, @PathVariable long categoryId) {
        deleteCategoryService.doService(userId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@RequestHeader("userId") Long userId, @RequestBody CategoryRequest request, @PathVariable long categoryId) {
        updateCategoryService.doService(userId, categoryId, request.categoryName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FindAllCategoriesResponse>> findAllCategories(@RequestHeader("userId") Long userId) {
        List<FindAllCategoriesResponse> response = findAllCategoriesService.doService(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
