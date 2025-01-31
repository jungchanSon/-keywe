package com.kiosk.server.store.controller;

import com.kiosk.server.store.controller.dto.CategoryRequest;
import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;
import com.kiosk.server.store.service.CreateCategoryService;
import com.kiosk.server.store.service.DeleteCategoryService;
import com.kiosk.server.store.service.FindAllCategoriesService;
import com.kiosk.server.store.service.UpdateCategoryService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<String> insertCategory(HttpServletRequest servletRequest, CategoryRequest request) {
        long userId = Long.parseLong(servletRequest.getParameter("userId"));
        long categoryId = createCategoryService.doService(userId, request.categoryName());
        return ResponseEntity.status(HttpStatus.CREATED).body(String.valueOf(categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(HttpServletRequest servletRequest, @PathVariable int categoryId) {
        long userId = Long.parseLong(servletRequest.getParameter("userId"));
        deleteCategoryService.doService(userId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(HttpServletRequest servletRequest, CategoryRequest request, @PathVariable int categoryId) {
        long userId = Long.parseLong(servletRequest.getParameter("userId"));
        updateCategoryService.doService(userId, categoryId, request.categoryName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FindAllCategoriesResponse>> findAllCategories(HttpServletRequest servletRequest) {
        long userId = Long.parseLong(servletRequest.getParameter("userId"));
        List<FindAllCategoriesResponse> response = findAllCategoriesService.doService(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
