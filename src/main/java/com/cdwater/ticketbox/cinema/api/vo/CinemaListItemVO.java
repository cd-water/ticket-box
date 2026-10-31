package com.cdwater.ticketbox.cinema.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CinemaListItemVO {

    private Long id;
    private String name;
    private String address;
    private String phone;
}
