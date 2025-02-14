package com.kiosk.server.service;

import com.kiosk.server.dto.NotificationMessage;
import com.kiosk.server.dto.SendFcmMessageResult;
import com.kiosk.server.dto.TargetType;

import java.util.List;

public interface SendFcmMessageService {

    SendFcmMessageResult doService(TargetType targetType, List<Long> targetIds, NotificationMessage message);
}
