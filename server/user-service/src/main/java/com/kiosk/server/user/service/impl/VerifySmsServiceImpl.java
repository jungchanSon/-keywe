package com.kiosk.server.user.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.user.service.VerifySmsService;
import com.kiosk.server.user.util.UserValidateUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifySmsServiceImpl implements VerifySmsService {

    private final UserValidateUtil userValidateUtil;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecretKey;

    @Value("${coolsms.sender.phone}")
    private String senderPhoneNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        log.info("CoolSMS 메시지 서비스를 초기화합니다.");
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
        log.info("CoolSMS 메시지 서비스가 성공적으로 초기화되었습니다.");
    }

    @Override
    public void doService(String phone, String verificationCode) {
        log.info("SMS 전송 시작 - 수신번호: {}, 인증코드: {}", userValidateUtil.maskPhoneNumber(phone), verificationCode);
        log.info("메시지 서비스 상태 - {}", messageService != null ? "초기화됨" : "초기화 안됨");

        Message message = new Message();
        message.setFrom(senderPhoneNumber);
        message.setTo(phone);
        message.setText("[KeyWe] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        try {
            log.info("발신번호: {}, 수신번호: {}", userValidateUtil.maskPhoneNumber(senderPhoneNumber), userValidateUtil.maskPhoneNumber(phone));

            SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);
            log.info("요청 객체 생성 완료");

            this.messageService.sendOne(request);
            log.info("{} 번호로 SMS가 성공적으로 전송되었습니다.", userValidateUtil.maskPhoneNumber(phone));
        } catch (Exception e) {
            log.error("SMS 전송 중 예외 발생", e);
            throw new BadRequestException("SMS 전송에 실패했습니다: " + e.getMessage());
        }
    }
}
