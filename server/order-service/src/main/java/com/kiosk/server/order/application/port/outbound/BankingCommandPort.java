package com.kiosk.server.order.application.port.outbound;

public interface BankingCommandPort {
    Long withdraw(long userId, long totalPrice);

    Long deposit(long userId, long totalPrice);
}