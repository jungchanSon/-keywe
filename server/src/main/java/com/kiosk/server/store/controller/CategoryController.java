package com.kiosk.server.store.controller;

import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;
import com.kiosk.server.store.service.CreateCategoryService;
import com.kiosk.server.store.service.DeleteCategoryService;
import com.kiosk.server.store.service.FindAllCategoriesService;
import com.kiosk.server.store.service.UpdateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Integer> insertCategory(@RequestParam("categoryName") String categoryName,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) {
        int categoryId = createCategoryService.doService(categoryName, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId) {
        String ceo = "CEO";
        deleteCategoryService.doService(ceo, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Integer> updateCategory(@RequestParam("categoryName") String categoryName,
                                                  @RequestParam(value = "image", required = false) MultipartFile image,
                                                  @PathVariable int categoryId) {
        updateCategoryService.doService(categoryName, image, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryId);
    }

    @GetMapping
    public ResponseEntity<List<FindAllCategoriesResponse>> findAllCategories() {
        List<FindAllCategoriesResponse> response = findAllCategoriesService.doService();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
