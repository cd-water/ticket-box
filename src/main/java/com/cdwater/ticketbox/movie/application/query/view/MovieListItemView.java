package com.cdwater.ticketbox.movie.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieListItemView {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
}
