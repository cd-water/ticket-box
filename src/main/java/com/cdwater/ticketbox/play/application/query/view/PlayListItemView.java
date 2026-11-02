package com.cdwater.ticketbox.play.application.query.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlayListItemView {

    private Long id;
    private String title;
    private String cover;
    private String address;
    private BigDecimal price;
    private Integer stock;
}
