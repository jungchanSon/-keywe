package com.kiosk.server.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiosk.server.store.controller.dto.*;
import com.kiosk.server.store.service.*;
import jakarta.servlet.http.HttpServletRequest;
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
    @PostMapping
    public ResponseEntity<CreateMenuResponse> insertMenu(HttpServletRequest request, @RequestPart("menu") String json,
                                                         @RequestPart(required = false) MultipartFile image) throws IOException {
        // JSON 문자열 DTO로 반환
        ObjectMapper mapper = new ObjectMapper();
        CreateMenuRequest dto = mapper.readValue(json, CreateMenuRequest.class);

        long userId = extractUserId(request);
        CreateMenuResponse response = createMenuService.doService(userId, dto, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 메뉴 전체 조회
    @GetMapping
    public ResponseEntity<List<MenuDetailResponse>> findAllMenu(HttpServletRequest request) {
        long userId = extractUserId(request);
        List<MenuDetailResponse> response = findMenusService.doService(userId, null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 카테고리 메뉴 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuDetailResponse>> findByCategory(HttpServletRequest request, @PathVariable long categoryId) {
        long userId = extractUserId(request);
        List<MenuDetailResponse> response = findMenusService.doService(userId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 메뉴 단건 상세조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailResponse> getMenuDetail(HttpServletRequest request, @PathVariable long menuId) {
        long userId = extractUserId(request);
        MenuDetailResponse response = findMenuDetailService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 메뉴 수정
    @PatchMapping("/{menuId}")
    public ResponseEntity<Void> updateMenu(HttpServletRequest request, @RequestPart("menu") String json,
                                           @RequestPart(required = false) MultipartFile image, @PathVariable long menuId) throws IOException {
        // JSON 문자열 DTO로 반환
        ObjectMapper mapper = new ObjectMapper();
        UpdateMenuRequest dto = mapper.readValue(json, UpdateMenuRequest.class);
        long userId = extractUserId(request);
        updateMenuService.doService(userId, menuId, dto, image);
        return ResponseEntity.ok().build();
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Long> deleteMenu(HttpServletRequest request, @PathVariable long menuId) {
        long userId = extractUserId(request);
        deleteMenuService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(menuId);
    }

    // 옵션 추가
    @PostMapping("/{menuId}/options")
    public ResponseEntity<CreateMenuResponse> addOption(HttpServletRequest request, @PathVariable long menuId, @RequestBody MenuOptionRequest menuRequest){
     long userId = extractUserId(request);
     CreateMenuResponse response = addOptionService.doService(userId, menuId, menuRequest);
     return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 옵션 수정
    @PatchMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<CreateMenuResponse> updateOption(HttpServletRequest request, @PathVariable long menuId, @PathVariable long optionId, @RequestBody MenuOptionRequest menuRequest){
        long userId = extractUserId(request);
        CreateMenuResponse response = updateOptionService.doService(userId, menuId, optionId, menuRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 옵션 삭제
    @DeleteMapping("/{menuId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(HttpServletRequest request, @PathVariable long menuId, @PathVariable long optionId, @RequestBody MenuOptionRequest menuRequest){
        long userId = extractUserId(request);
        deleteOptionService.doService(userId, menuId, optionId, menuRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 이미지 삭제
    @DeleteMapping("/{menuId}/image")
    public ResponseEntity<Void> deleteImage(HttpServletRequest request, @PathVariable long menuId){
        long userId = extractUserId(request);
        deleteImageService.doService(userId, menuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private long extractUserId(HttpServletRequest request) {
        return (long) request.getAttribute("userId");
    }

}
