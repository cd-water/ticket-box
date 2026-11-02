package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.util.List;

@Data
public class CinemaDetailDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String phone;
    private List<CinemaMovieDTO> movies;
}
