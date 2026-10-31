package com.cdwater.ticketbox.movie.api.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieListItemVO {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
}
