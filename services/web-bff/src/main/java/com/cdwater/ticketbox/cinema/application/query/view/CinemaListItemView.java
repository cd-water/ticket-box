package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CinemaListItemView {

    private Long id;
    private String name;
    private String address;
    private String phone;
}
