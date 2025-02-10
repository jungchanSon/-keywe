package com.kiosk.server.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.kiosk.server.domain.RemoteOrderSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendToFamily(RemoteOrderSession session) {
        // FCM 발송 로직
        Message message = Message.builder()
            .setTopic("family_" + session.getFamilyId())  // 가족 그룹으로 전송
            .setNotification(Notification.builder()
                .setTitle("대리 주문 도움 요청")
                .setBody("키오스크 주문 도움이 필요합니다.")
                .build())
            .putData("sessionId", session.getSessionId())
            .putData("storeId", session.getStoreId())
            .putData("kioskUserId", session.getKioskUserId())
            .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM send failed", e);
        }
    }
}
