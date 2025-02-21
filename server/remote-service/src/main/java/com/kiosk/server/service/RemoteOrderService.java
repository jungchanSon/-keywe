package com.kiosk.server.service;

import com.kiosk.server.client.feign.api.UserServiceClient;
import com.kiosk.server.client.feign.dto.UserProfile;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.domain.RemoteOrderSession;
import com.kiosk.server.domain.RemoteOrderSessionRepository;
import com.kiosk.server.domain.RemoteOrderStatus;
import com.kiosk.server.event.RemoteOrderRequestedEvent;
import com.kiosk.server.websocket.exception.*;
import com.kiosk.server.websocket.message.response.RemoteOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemoteOrderService {

    private final TaskScheduler taskScheduler;
    private final UserServiceClient userServiceClient;
    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final RemoteOrderSessionRepository sessionRepository;

    private static final Duration SESSION_TIMEOUT = Duration.ofSeconds(30);

    public RemoteOrderSession saveSession(String userId, String familyId, String storeId) {
        log.info("대리 주문 요청 처리 시작");

        UserProfile userProfile = userServiceClient.getUserProfile(userId);
        if (userProfile == null) {
            log.info("부모 프로필 조회 실패 - profileId: {}", userId);
        }

        if (!userProfile.role().equals("PARENT")) {
            log.info("부모 프로필 검증 실패 : - role: {}", userProfile.role());
            throw new ChildRemoteOrderForbiddenException();
        }

        RemoteOrderSession session = RemoteOrderSession.builder()
            .sessionId(String.valueOf(IdUtil.create()))
            .familyId(familyId)
            .kioskUserId(userId)
            .kioskUserName(userProfile.name())
            .storeId(storeId)
            .status(RemoteOrderStatus.WAITING.name())
            .createdAt(LocalDateTime.now().toString())
            .build();

        sessionRepository.save(session);

        List<UserProfile> helperProfiles = userServiceClient.getHelperProfiles(familyId);

        if(helperProfiles == null || helperProfiles.isEmpty()) {
            log.info("자식 프로필 조회 실패 - helperProfilesSize: {}", helperProfiles == null ? -1 : 0);
            throw new HelperNotFoundException();
        }
        log.info("자식 프로필 조회 성공 - helperProfilesSize: {}", helperProfiles.size());

        sessionRepository.saveHelperIdNameMap(familyId, helperProfiles);

        scheduleTimeoutCheck(session.getSessionId());
        eventPublisher.publishEvent(new RemoteOrderRequestedEvent(session));

        return session;
    }

    public RemoteOrderSession acceptSession(String sessionId, String helperUserId, String familyId) {
        // Helper의 이름 가져오기
        String helperName = sessionRepository.getHelperName(familyId, helperUserId);
        if (helperName == null) {
            log.info("등록된 자식 프로필 아님 - helperUserId: {}", helperUserId);
            throw new UnauthorizedRemoteOrderAcceptException();
        }

        return sessionRepository.acceptSession(sessionId, helperUserId, helperName);
    }

    private void scheduleTimeoutCheck(String sessionId) {
        taskScheduler.schedule(
            () -> checkTimeout(sessionId),
            Instant.now().plusSeconds(SESSION_TIMEOUT.toSeconds())
        );
    }

    private void checkTimeout(String sessionId) {
        RemoteOrderSession session = sessionRepository.findById(sessionId);

        if (session != null && RemoteOrderStatus.WAITING.name().equals(session.getStatus())) {
            log.info("세션 타임 아웃 발생 - sessionId: {}", session.getSessionId());
            session.setStatus(RemoteOrderStatus.TIMEOUT.name());
            sessionRepository.saveWithTTL(session);

            messagingTemplate.convertAndSend(
                "/topic/" + session.getKioskUserId(),
                RemoteOrderResponse.error(RemoteOrderError.HELPER_NOT_FOUND)
            );
        }
    }
}
