package com.kiosk.server.common.exception;

import exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PushNotificationDeliveryException.class)
    public ResponseEntity<ErrorResponse> handleException(PushNotificationDeliveryException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        log.error(errors.toString());
//        ErrorResponse errorResponse = new ErrorResponse("Unexpected Error");
        ErrorResponse errorResponse = new ErrorResponse(errors.toString());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
