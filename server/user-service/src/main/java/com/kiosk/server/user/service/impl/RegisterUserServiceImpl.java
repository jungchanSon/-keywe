package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.ConflictException;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserServiceImpl implements RegisterUserService {

    private final UserRepository userRepository;

    public void doService(String email, String password) {
        log.info("RegisterUserService: email={}", email);

        checkEmailDuplication(email);
        verifyFormat(email, password);

        User user = User.create(email, password);

        userRepository.registerUser(user);
        log.info("사용자 등록 완료 - email={}", email);

    }

    // 이메일 중복 체크
    private void checkEmailDuplication(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("중복된 이메일 등록 시도 - email={}", email);
            throw new ConflictException("중복된 이메일입니다. 다른 이메일을 입력해 주세요.");
        }
    }

    /*
     * [이메일 패턴 확인]
     * - 영문자, 숫자, 점(.), 언더스코어(_), 하이픈(-) 허용
     * - @ 기호 필수
     * - 도메인 부분에 영문자, 숫자, 점, 하이픈 허용
     * - 최상위 도메인은 2-6자 사이의 영문자만 허용
     *
     * [비밀번호 패턴 확인]
     * - 대소문자, 숫자, 특수문자를 각각 1개 이상 포함
     * - 전체 길이는 8 ~ 16자 사이
     * */
    private void verifyFormat(String email, String password) {

        String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String passRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[~!@#$%^&*_\\-+=`|\\\\(){}\\[\\]:;\"'<>,.?/]).{8,16}$";

        if (email == null || !email.matches(emailRegex)) {
            log.warn("유효하지 않은 이메일 입력 - email={}", email);
            throw new BadRequestException("이메일 형식이 올바르지 않습니다. 올바른 이메일 형식으로 입력해 주세요.");
        }

        if (password == null || !password.matches(passRegex)) {
            log.warn("유효하지 않은 패스워드 입력 - email={}", email);
            throw new BadRequestException("패스워드 형식이 올바르지 않습니다. 올바른 패스워드 형식으로 입력해 주세요.");
        }
    }
}
