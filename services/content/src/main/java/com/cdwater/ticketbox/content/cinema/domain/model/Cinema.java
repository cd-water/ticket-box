package com.cdwater.ticketbox.content.cinema.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cinema {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public boolean isOperating() {
        return Integer.valueOf(1).equals(status);
    }
}
