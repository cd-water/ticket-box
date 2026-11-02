package com.cdwater.ticketbox.cinema.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
    private Integer duration;
    private Integer status;
}
