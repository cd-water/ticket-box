package com.cdwater.ticketbox.home.application.query.view;

import lombok.Data;

import java.util.List;

@Data
public class HomeView {

    private List<BannerView> banners;
    private List<HomeMovieItem> movies;
    private List<HomeShowItem> shows;
    private List<HomePlayItem> plays;
}
