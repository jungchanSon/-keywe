package com.kiosk.server.service.impl;

import com.google.firebase.messaging.*;
import com.kiosk.server.common.exception.PushNotificationDeliveryException;
import com.kiosk.server.domain.PushToken;
import com.kiosk.server.domain.PushTokenRepository;
import com.kiosk.server.dto.NotificationMessage;
import com.kiosk.server.dto.SendFcmMessageResult;
import com.kiosk.server.dto.TargetType;
import com.kiosk.server.event.InvalidPushTokensDetectedEvent;
import com.kiosk.server.service.SendFcmMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendFcmMessageServiceImpl implements SendFcmMessageService {

    private final FirebaseMessaging firebaseMessaging;
    private final PushTokenRepository pushTokenRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public SendFcmMessageResult doService(TargetType targetType, Long targetId, NotificationMessage message) {
        List<PushToken> pushTokens = switch (targetType) {
            case USER -> pushTokenRepository.findByUserId(targetId);
            case PROFILE -> pushTokenRepository.findByProfileId(targetId);
        };

        if (pushTokens.isEmpty()) {
            return new SendFcmMessageResult(0, 0);
        }

        MulticastMessage fcmMessage = buildMulticastMessage(pushTokens, message);

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(fcmMessage);
            return handleBatchResponse(response, pushTokens);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message: {}", e.getMessage());
            throw new PushNotificationDeliveryException("Failed to send FCM message, " + e.getMessage());
        }
    }

    private MulticastMessage buildMulticastMessage(List<PushToken> pushTokens, NotificationMessage message) {
        return MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(message.title())
                    .setBody(message.body())
                    .build()
            )
            .putAllData(message.data())
            .setAndroidConfig(getAndroidConfig(message))
            .addAllTokens(pushTokens.stream().map(PushToken::getToken).toList())
            .build();
    }

    private AndroidConfig getAndroidConfig(NotificationMessage message) {
        AndroidConfig.Builder builder = AndroidConfig.builder()
            .setNotification(AndroidNotification.builder()
                .setChannelId(message.channelId()).build());

        if (message.priority() != null) {
            builder.setPriority(message.priority() == NotificationMessage.MessagePriority.HIGH ?
                AndroidConfig.Priority.HIGH : AndroidConfig.Priority.NORMAL);
        }

        if (message.timeToLive() != null) {
            builder.setTtl(message.timeToLive() * 1000L);
        }

        return builder.build();
    }

    protected SendFcmMessageResult handleBatchResponse(BatchResponse response, List<PushToken> pushTokens) {
        List<PushToken> invalidPushTokens = new ArrayList<>();

        List<SendResponse> responses = response.getResponses();

        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).isSuccessful()) {
                continue;
            }

            SendResponse failedResponse = responses.get(i);

            FirebaseMessagingException exception = failedResponse.getException();
            log.error("Failed to send message to token {}: {}",
                pushTokens.get(i).getToken(),
                exception.getMessage());

            MessagingErrorCode errorCode = exception.getMessagingErrorCode();
            if (errorCode == MessagingErrorCode.INVALID_ARGUMENT || errorCode == MessagingErrorCode.UNREGISTERED) {
                invalidPushTokens.add(pushTokens.get(i));
            }
        }

        log.info("Successfully sent messages: {}/{}",
            response.getSuccessCount(),
            response.getFailureCount() + response.getSuccessCount());

        // 유효하지 않은 토큰 삭제 처리 이벤트
        eventPublisher.publishEvent(new InvalidPushTokensDetectedEvent(invalidPushTokens));

        return new SendFcmMessageResult(response.getSuccessCount(), response.getFailureCount());
    }
}
