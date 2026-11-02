package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.MovieDetailDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListQueryDTO;

import java.util.Collection;
import java.util.Map;

public interface MovieQueryApi {

    PageResult<MovieListItemDTO> list(MovieListQueryDTO query) throws BizException;

    MovieDetailDTO detail(long movieId) throws BizException;

    Map<Long, MovieBriefDTO> findBriefs(Collection<Long> movieIds) throws BizException;
}
