package com.cdwater.ticketbox.order.show.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShowOrderStatus {

    PENDING(0),
    PAID(1),
    CANCELLED(2);

    private final int code;
}
