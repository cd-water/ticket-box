package com.cdwater.ticketbox.movie.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDetailView {

    private Long id;
    private String title;
    private String poster;
    private String synopsis;
    private Integer duration;
    private LocalDateTime releaseDate;
    private String tab;
    private List<GenreView> genres;
    private List<PersonView> directors;
    private List<PersonView> actors;
    private long reviewCount;
}
