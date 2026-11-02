package com.cdwater.ticketbox.content.movie.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.MovieQueryApi;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.MovieDetailDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListQueryDTO;
import com.cdwater.ticketbox.content.movie.application.query.MovieBriefQueryService;
import com.cdwater.ticketbox.content.movie.application.query.MovieCatalogQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;
import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class MovieQueryApiImpl implements MovieQueryApi {

    private final MovieCatalogQueryService movieCatalogQueryService;
    private final MovieBriefQueryService movieBriefQueryService;

    @Override
    public PageResult<MovieListItemDTO> list(MovieListQueryDTO query) {
        return movieCatalogQueryService.list(query);
    }

    @Override
    public MovieDetailDTO detail(long movieId) {
        return movieCatalogQueryService.detail(movieId);
    }

    @Override
    public Map<Long, MovieBriefDTO> findBriefs(Collection<Long> movieIds) {
        return movieBriefQueryService.findByIds(movieIds);
    }
}
