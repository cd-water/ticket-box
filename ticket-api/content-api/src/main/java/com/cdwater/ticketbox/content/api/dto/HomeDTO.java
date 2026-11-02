package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.util.List;

@Data
public class HomeDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<BannerDTO> banners;
    private List<HomeMovieDTO> movies;
    private List<HomeShowDTO> shows;
    private List<HomePlayDTO> plays;
}
