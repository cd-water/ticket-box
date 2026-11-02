package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieBriefDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String poster;
    private LocalDateTime releaseDate;
    private Integer duration;
    private Integer status;
}
