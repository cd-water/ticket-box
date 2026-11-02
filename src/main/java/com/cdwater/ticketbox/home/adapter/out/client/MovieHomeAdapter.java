package com.cdwater.ticketbox.home.adapter.out.client;

import com.cdwater.ticketbox.home.adapter.out.client.converter.MovieHomeConverter;
import com.cdwater.ticketbox.home.application.port.HomeMoviePort;
import com.cdwater.ticketbox.home.application.query.view.HomeMovieItem;
import com.cdwater.ticketbox.movie.application.query.MovieCatalogQueryService;
import com.cdwater.ticketbox.movie.application.query.MovieListQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieHomeAdapter implements HomeMoviePort {

    private final MovieCatalogQueryService movieCatalogQueryService;
    private final MovieHomeConverter movieHomeConverter;

    @Override
    public List<HomeMovieItem> findShown(int limit) {
        MovieListQuery query = new MovieListQuery();
        query.setTab("showing");
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return movieCatalogQueryService.list(query).getRecords().stream()
                .map(movieHomeConverter::toItem)
                .toList();
    }
}
