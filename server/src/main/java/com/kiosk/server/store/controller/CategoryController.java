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
    public ResponseEntity<Integer> insertCategory(CategoryRequest request) {
        int categoryId = createCategoryService.doService(request.categoryName());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId) {
        String ceo = "CEO";
        deleteCategoryService.doService(ceo, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Integer> updateCategory(CategoryRequest request, @PathVariable int categoryId) {
        updateCategoryService.doService(request.categoryName(), categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryId);
    }

    @GetMapping
    public ResponseEntity<List<FindAllCategoriesResponse>> findAllCategories() {
        List<FindAllCategoriesResponse> response = findAllCategoriesService.doService();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
