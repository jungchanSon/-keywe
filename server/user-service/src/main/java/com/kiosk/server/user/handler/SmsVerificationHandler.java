package com.kiosk.server.user.handler;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.SmsException;
import com.kiosk.server.user.controller.dto.SmsResponse;
import com.kiosk.server.user.domain.SmsAuthenticationRepository;
import com.kiosk.server.user.service.VerifySmsService;
import com.kiosk.server.user.util.UserValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Slf4j
@Component
@RequiredArgsConstructor
public class SmsVerificationHandler {

    private final VerifySmsService verifySmsService;
    private final UserValidateUtil userValidateUtil;
    private final SmsAuthenticationRepository smsAuthenticationRepository;
    private final SecureRandom random = new SecureRandom();

    // SMS 인증 코드 생성 및 전송
    public SmsResponse sendVerificationSms(String phone) {
        // 전화번호 마스킹 로깅
        log.info("휴대폰 번호 {}로 인증번호 전송을 시작합니다.", userValidateUtil.maskPhoneNumber(phone));

        // 전화번호 유효성 검사
        userValidateUtil.validatePhoneNumber(phone);

        // rate limit 체크
        if (!smsAuthenticationRepository.checkRateLimit(phone)) {
            throw new BadRequestException("인증번호 발송 횟수를 초과했습니다. 3분 후에 다시 시도해주세요.");
        }

        try {
            // 인증번호 생성 및 저장
            String verificationCode = generateVerificationCode();
            smsAuthenticationRepository.saveAuthenticationCode(phone, verificationCode);

            // SMS 발송
            verifySmsService.doService(phone, verificationCode);
            log.info("휴대폰 번호 {}로 인증번호 전송이 완료되었습니다.", userValidateUtil.maskPhoneNumber(phone));

            return new SmsResponse("인증번호가 전송되었습니다.");
        } catch (Exception e) {
            log.error("휴대폰 번호 {}로 인증번호 전송에 실패했습니다: {}",
                    userValidateUtil.maskPhoneNumber(phone), e.getMessage(), e);
            throw new SmsException("SMS 전송에 실패했습니다.");
        }
    }

    // SMS 인증 코드 검증
    public SmsResponse verifySmsCode(long userId, String phone, String inputCode) {
        log.info("휴대폰 번호 {}의 인증번호를 검증합니다.", userValidateUtil.maskPhoneNumber(phone));

        // 저장된 인증번호 조회
        String storedCode = smsAuthenticationRepository.getAuthenticationCode(phone);
        if (storedCode == null) {
            throw new EntityNotFoundException("인증번호가 존재하지 않습니다. 인증번호를 다시 요청해주세요.");
        }

        // 인증번호 일치 여부 확인
        boolean isVerified = storedCode.equals(inputCode);
        if (isVerified) {
            smsAuthenticationRepository.deleteAuthenticationCode(phone);
            smsAuthenticationRepository.markPhoneNumberAsVerified(userId, phone);
            log.info("휴대폰 번호 {}의 인증이 성공적으로 완료되었습니다.", userValidateUtil.maskPhoneNumber(phone));
            return new SmsResponse("인증이 완료되었습니다.");
        } else {
            log.warn("휴대폰 번호 {}의 잘못된 인증번호가 입력되었습니다.", userValidateUtil.maskPhoneNumber(phone));
            return new SmsResponse("잘못된 인증번호입니다. 다시 시도해주세요.");
        }

    }

    // 6자리 인증코드 생성
    private String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
