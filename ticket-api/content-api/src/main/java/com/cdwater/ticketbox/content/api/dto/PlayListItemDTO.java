package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlayListItemDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String cover;
    private String address;
    private BigDecimal price;
    private Integer stock;
}
