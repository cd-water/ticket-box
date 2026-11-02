package com.cdwater.ticketbox.cinema.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Hall {

    private Long id;
    private Long cinemaId;
    private String name;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
