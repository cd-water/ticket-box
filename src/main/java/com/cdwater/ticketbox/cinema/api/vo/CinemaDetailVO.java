package com.cdwater.ticketbox.cinema.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class CinemaDetailVO {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private List<CinemaMovieVO> movies;
}
