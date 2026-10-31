package com.cdwater.ticketbox.movie.api.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDetailVO {

    private Long id;
    private String title;
    private String poster;
    private String synopsis;
    private Integer duration;
    private LocalDateTime releaseDate;
    private String tab;
    private List<GenreVO> genres;
    private List<CrewVO> directors;
    private List<CrewVO> actors;
    private long reviewCount;
}
