package com.cdwater.ticketbox.content.home.application.query;

import com.cdwater.ticketbox.content.home.application.port.HomeMoviePort;
import com.cdwater.ticketbox.content.home.application.port.HomePlayPort;
import com.cdwater.ticketbox.content.home.application.port.HomeShowPort;
import com.cdwater.ticketbox.content.api.dto.BannerDTO;
import com.cdwater.ticketbox.content.api.dto.HomeDTO;
import com.cdwater.ticketbox.content.home.application.query.converter.HomeViewConverter;
import com.cdwater.ticketbox.content.home.domain.repository.BannerRepository;
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

    public HomeDTO getHome() {
        HomeDTO view = new HomeDTO();
        view.setBanners(homeViewConverter.toBannerViews(bannerRepository.findEnabled()));
        view.setMovies(moviePort.findShown(LIMIT));
        view.setShows(showPort.findUpcoming(LIMIT));
        view.setPlays(playPort.findOnSale(LIMIT));
        return view;
    }
}