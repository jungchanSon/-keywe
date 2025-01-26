package com.kiosk.server.user.domain;

import com.kiosk.server.user.controller.dto.UserProfileResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserProfileRepository {

    // 프로필 생성
    void createNewProfile(UserProfile userProfile);

    // 프로필 이름 조회 (중복 비허용)
    int checkDuplicateProfileName(@Param("userId") long userId, @Param("profileName") String profileName);

    // 프로필 목록 조회
    List<UserProfileResponse> getUserProfileList(long userId);

    // 프로필 상세 조회 / 선택
    UserProfile findUserProfileById(Map<String, Object> idParams);

    // 자식 프로필 상세 수정
    void updateChildProfile(Map<String, Object> profileParams);

    // 부모 프로필 상세 수정
    void updateParentProfile(Map<String, Object> profileParams);

    // 프로필 삭제
    void deleteUserProfileById(Map<String, Object> idParams);
}