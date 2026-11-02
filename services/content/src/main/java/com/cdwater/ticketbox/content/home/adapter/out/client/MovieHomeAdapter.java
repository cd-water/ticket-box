package com.cdwater.ticketbox.content.home.adapter.out.client;
import com.cdwater.ticketbox.content.api.dto.MovieListQueryDTO;

import com.cdwater.ticketbox.content.home.adapter.out.client.converter.MovieHomeConverter;
import com.cdwater.ticketbox.content.home.application.port.HomeMoviePort;
import com.cdwater.ticketbox.content.api.dto.HomeMovieDTO;
import com.cdwater.ticketbox.content.movie.application.query.MovieCatalogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieHomeAdapter implements HomeMoviePort {

    private final MovieCatalogQueryService movieCatalogQueryService;
    private final MovieHomeConverter movieHomeConverter;

    @Override
    public List<HomeMovieDTO> findShown(int limit) {
        MovieListQueryDTO query = new MovieListQueryDTO();
        query.setTab("showing");
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return movieCatalogQueryService.list(query).getRecords().stream()
                .map(movieHomeConverter::toItem)
                .toList();
    }
}