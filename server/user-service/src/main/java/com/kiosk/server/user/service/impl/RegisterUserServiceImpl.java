package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.ConflictException;
import com.kiosk.server.user.domain.User;
import com.kiosk.server.user.domain.UserRepository;
import com.kiosk.server.user.service.RegisterUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterUserServiceImpl implements RegisterUserService {

    private final UserRepository userRepository;

    public void doService(String email, String password) {

        checkEmailDuplication(email);
        verifyFormat(email, password);

        User user = User.create(email, password);

        userRepository.registerUser(user);

    }

    // 이메일 중복 체크
    private void checkEmailDuplication(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email Duplicated");
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
            throw new BadRequestException("Invalid Email Format");
        }

        if (password == null || !password.matches(passRegex)) {
            throw new BadRequestException("Invalid Password Format");
        }
    }

}
