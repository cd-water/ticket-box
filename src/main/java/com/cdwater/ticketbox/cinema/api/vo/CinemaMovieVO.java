package com.cdwater.ticketbox.cinema.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CinemaMovieVO {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
    private Integer duration;
}
