package com.cdwater.ticketbox.home.application.query;

import com.cdwater.ticketbox.home.application.port.HomeMoviePort;
import com.cdwater.ticketbox.home.application.port.HomePlayPort;
import com.cdwater.ticketbox.home.application.port.HomeShowPort;
import com.cdwater.ticketbox.home.application.query.view.BannerView;
import com.cdwater.ticketbox.home.application.query.view.HomeView;
import com.cdwater.ticketbox.home.application.query.converter.HomeViewConverter;
import com.cdwater.ticketbox.home.domain.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeQueryService {

    private static final int LIMIT = 8;

    private final BannerRepository bannerRepository;
    private final HomeViewConverter homeViewConverter;
    private final HomeMoviePort moviePort;
    private final HomeShowPort showPort;
    private final HomePlayPort playPort;

    public HomeView home() {
        HomeView view = new HomeView();
        view.setBanners(homeViewConverter.toBannerViews(bannerRepository.findEnabled()));
        view.setMovies(moviePort.findShown(LIMIT));
        view.setShows(showPort.findUpcoming(LIMIT));
        view.setPlays(playPort.findOnSale(LIMIT));
        return view;
    }
}
