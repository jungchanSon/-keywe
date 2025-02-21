package com.kiosk.server.service;

public interface EmailSender {

    void send(String to, String subject, String content) throws Exception;
}
