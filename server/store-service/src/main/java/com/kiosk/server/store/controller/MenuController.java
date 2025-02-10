package com.kiosk.server.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiosk.server.store.controller.dto.*;
import com.kiosk.server.store.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            @RequestPart("menu") String json,
            @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        // JSON 문자열 DTO로 반환
        ObjectMapper mapper = new ObjectMapper();
        CreateMenuRequest dto = mapper.readValue(json, CreateMenuRequest.class);
        CreateMenuResponse response = createMenuService.doService(userId, dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 메뉴, 카테고리 메뉴 조회
    @GetMapping
    public ResponseEntity<List<MenuResponse>> findMenus(
            @RequestHeader("userId") Long userId,
            @RequestParam(value = "cid", required = false) Long categoryId) {
        List<MenuResponse> responses = findMenusService.doService(userId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    // 메뉴 단건 상세조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuDetail(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
        MenuDetailResponse response = findMenuDetailService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 메뉴 수정
    @PatchMapping(path = "/{menuId}", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateMenu(
            @RequestHeader("userId") Long userId,
            @RequestPart("menu") String json,
            @RequestPart(required = false) MultipartFile image,
            @PathVariable long menuId
    ) throws IOException {
        // JSON 문자열 DTO로 반환
        ObjectMapper mapper = new ObjectMapper();
        UpdateMenuRequest dto = mapper.readValue(json, UpdateMenuRequest.class);
        updateMenuService.doService(userId, menuId, dto, image);
        return ResponseEntity.ok().build();
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Long> deleteMenu(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
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
        deleteOptionService.doService(userId, menuId, optionId, menuRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 이미지 삭제
    @DeleteMapping("/{menuId}/image")
    public ResponseEntity<Void> deleteImage(@RequestHeader("userId") Long userId, @PathVariable long menuId) {
        deleteImageService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
