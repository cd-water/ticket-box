package com.cdwater.ticketbox.content.home.application.port;

import com.cdwater.ticketbox.content.api.dto.HomeMovieDTO;

import java.util.List;

public interface HomeMoviePort {

    List<HomeMovieDTO> findShown(int limit);
}