package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.Data;

import java.util.List;

@Data
public class CinemaDetailView {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private List<CinemaMovieView> movies;
}
