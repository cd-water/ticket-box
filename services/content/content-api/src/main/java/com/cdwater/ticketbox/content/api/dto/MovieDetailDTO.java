package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDetailDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String poster;
    private String synopsis;
    private Integer duration;
    private LocalDateTime releaseDate;
    private String tab;
    private List<GenreDTO> genres;
    private List<PersonDTO> directors;
    private List<PersonDTO> actors;
    private long reviewCount;
}
