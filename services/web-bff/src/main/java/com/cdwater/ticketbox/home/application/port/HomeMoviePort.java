package com.cdwater.ticketbox.home.application.port;

import com.cdwater.ticketbox.home.application.query.view.HomeMovieItem;

import java.util.List;

public interface HomeMoviePort {

    List<HomeMovieItem> findShown(int limit);
}
