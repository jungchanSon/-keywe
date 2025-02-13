package com.kiosk.server.common.exception;

public class UnauthorizedProfileAccessException extends RuntimeException {

    public UnauthorizedProfileAccessException(Long requestedProfileId, Long actualProfileId) {
        super(String.format("Profile access denied. Requested profile: %d does not match token's profile: %d",
            requestedProfileId, actualProfileId));
    }
}
