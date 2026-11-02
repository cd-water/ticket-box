package com.cdwater.ticketbox.order.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {

    PENDING(0),
    PAID(1),
    CANCELLED(2);

    private final int code;
}
