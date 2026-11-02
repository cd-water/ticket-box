package com.cdwater.ticketbox.inventory.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HoldStatus {

    HELD(0),
    CONFIRMED(1),
    RELEASED(2);

    private final int code;
}
