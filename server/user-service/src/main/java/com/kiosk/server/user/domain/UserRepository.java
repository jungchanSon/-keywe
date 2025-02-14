package com.kiosk.server.user.domain;

public interface UserRepository {

    // 회원 가입
    void registerUser(User user);

    // 아이디 중복 확인
    boolean existsByEmail(String email);

    // 아이디로 회원 조회
    User findByEmail(String email);

    // 회원 이메일 인증 완료 처리
    void verifyEmail(Long userId);

}
