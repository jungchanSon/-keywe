package com.kiosk.server.service;

import com.kiosk.server.dto.NotificationMessage;
import com.kiosk.server.dto.SendFcmMessageResult;
import com.kiosk.server.dto.TargetType;

public interface SendFcmMessageService {

    SendFcmMessageResult doService(TargetType targetType, Long targetId, NotificationMessage message);
}
