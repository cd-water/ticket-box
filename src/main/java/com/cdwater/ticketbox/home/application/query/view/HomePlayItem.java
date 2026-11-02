package com.cdwater.ticketbox.home.application.query.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomePlayItem {

    private Long id;
    private String title;
    private String cover;
    private String address;
    private BigDecimal price;
    private Integer stock;
}
