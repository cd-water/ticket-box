package com.cdwater.ticketbox.home.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeMovieItem {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
}
