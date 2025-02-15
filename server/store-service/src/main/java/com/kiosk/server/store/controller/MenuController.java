package com.kiosk.server.store.controller;

import com.kiosk.server.image.service.DeleteImageService;
import com.kiosk.server.store.controller.dto.*;
import com.kiosk.server.store.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final CreateMenuService createMenuService;
    private final FindMenusService findMenusService;
    private final FindMenuDetailService findMenuDetailService;
    private final UpdateMenuService updateMenuService;
    private final DeleteMenuService deleteMenuService;
    private final AddOptionService addOptionService;
    private final UpdateOptionService updateOptionService;
    private final DeleteOptionService deleteOptionService;
    private final DeleteImageService deleteImageService;

    // 메뉴 등록
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CreateMenuResponse> insertMenu(
        @RequestHeader("userId") Long userId,
        @RequestPart("menu") CreateMenuRequest request,
        @RequestPart(required = false) MultipartFile image
    ) {
        log.info("Request: userId={}, menuName={}", userId, request.menuName());
        CreateMenuResponse response = createMenuService.doService(userId, request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 메뉴, 카테고리 메뉴 조회
    @GetMapping
    public ResponseEntity<List<MenuResponse>> findMenus(
        @RequestHeader("userId") Long userId,
        @RequestParam(value = "cid", required = false) Long categoryId,
        @RequestParam(value = "sid", required = false) Long storeId
    ) {
        log.info("Request: userId={}, categoryId={}, storeId={}", userId, categoryId, storeId);
        Long actualStoreId = storeId != null ? storeId : userId;
        List<MenuResponse> responses = findMenusService.doService(actualStoreId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    // 메뉴 단건 상세조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuDetail(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
        log.info("Request: userId={}, findMenuId={}", userId, menuId);
        MenuDetailResponse response = findMenuDetailService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 메뉴 수정
    @PatchMapping(path = "/{menuId}", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateMenu(
        @RequestHeader("userId") Long userId,
        @RequestPart("menu") UpdateMenuRequest request,
        @RequestPart(required = false) MultipartFile image,
        @PathVariable long menuId
    ) {
        log.info("Request: userId={}, updateMenuId={}", userId, menuId);
        updateMenuService.doService(userId, menuId, request, image);
        return ResponseEntity.ok().build();
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Long> deleteMenu(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
        log.info("Request: userId={}, deleteMenuId={}", userId, menuId);
        deleteMenuService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(menuId);
    }

    // 옵션 추가
    @PostMapping("/{menuId}/options")
    public ResponseEntity<CreateMenuResponse> addOption(
        @RequestHeader("userId") Long userId,
        @PathVariable long menuId,
        @RequestBody MenuOptionRequest menuRequest
    ) {
        log.info("Request: userId={}, menuId={}, optionId={}", userId, menuId, menuRequest.optionId());
        CreateMenuResponse response = addOptionService.doService(userId, menuId, menuRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 옵션 수정
    @PatchMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<CreateMenuResponse> updateOption(
        @RequestHeader("userId") Long userId,
        @PathVariable long menuId,
        @PathVariable long optionId,
        @RequestBody MenuOptionRequest menuRequest
    ) {
        log.info("Request: userId={}, menuId={}, updateOptionId={}, updateOptionName={}", userId, menuId, optionId, menuRequest.optionName());
        CreateMenuResponse response = updateOptionService.doService(userId, menuId, optionId, menuRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 옵션 삭제
    @DeleteMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(
        @RequestHeader("userId") Long userId,
        @PathVariable long menuId,
        @PathVariable long optionId,
        @RequestBody MenuOptionRequest menuRequest
    ) {
        log.info("Request: userId={}, menuId={}, deleteOptionId={}", userId, menuId, optionId);
        deleteOptionService.doService(userId, menuId, optionId, menuRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 이미지 삭제
    @DeleteMapping("/{menuId}/image")
    public ResponseEntity<Void> deleteImage(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
        log.info("Request: userId={}, menuId={}", userId, menuId);
        deleteImageService.doService(userId, menuId,"menu");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
