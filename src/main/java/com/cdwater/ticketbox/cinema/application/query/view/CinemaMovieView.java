package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CinemaMovieView {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
    private Integer duration;
}
