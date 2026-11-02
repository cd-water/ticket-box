package com.cdwater.ticketbox.play.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlayOrderStatus {

    PENDING(0),
    PAID(1),
    CANCELLED(2);

    private final int code;
}
