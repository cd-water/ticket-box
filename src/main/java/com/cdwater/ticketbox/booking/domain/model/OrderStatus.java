package com.cdwater.ticketbox.booking.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    PENDING(0),
    PAID(1),
    CANCELLED(2),
    TIMEOUT(3);

    private final int code;
}
