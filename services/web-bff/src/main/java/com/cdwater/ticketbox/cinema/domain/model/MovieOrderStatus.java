package com.cdwater.ticketbox.cinema.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MovieOrderStatus {

    PENDING(0),
    PAID(1),
    CANCELLED(2);

    private final int code;
}
