package com.cdwater.ticketbox.content.movie.application.query;
import com.cdwater.ticketbox.content.api.dto.MovieListQueryDTO;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.movie.application.query.converter.MovieViewConverter;
import com.cdwater.ticketbox.content.api.dto.PersonDTO;
import com.cdwater.ticketbox.content.api.dto.MovieDetailDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import com.cdwater.ticketbox.content.movie.domain.model.Movie;
import com.cdwater.ticketbox.content.movie.domain.repository.MovieRepository;
import com.cdwater.ticketbox.content.movie.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieCatalogQueryService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final MovieViewConverter movieViewMapper;

    public PageResult<MovieListItemDTO> list(MovieListQueryDTO query) {
        PageResult<Movie> page = movieRepository.page(
                query.getTab(), query.getKeyword(), query.getPage(), query.getSize());
        List<MovieListItemDTO> records = page.getRecords().stream()
                .map(movieViewMapper::toListItemView)
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public MovieDetailDTO getDetail(Long id) {
        Movie movie = movieRepository.findById(id)
                .filter(Movie::isOnShelf)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));

        MovieDetailDTO view = movieViewMapper.toDetailView(movie);
        view.setTab(movie.isUpcoming(LocalDateTime.now()) ? "upcoming" : "showing");
        view.setGenres(movieRepository.findGenres(id).stream()
                .map(movieViewMapper::toView)
                .toList());

        List<PersonDTO> directors = new ArrayList<>();
        List<PersonDTO> actors = new ArrayList<>();
        movieRepository.findPersons(id).forEach(person -> {
            if (person.isDirector()) {
                directors.add(movieViewMapper.toView(person));
            } else if (person.isActor()) {
                actors.add(movieViewMapper.toView(person));
            }
        });
        view.setDirectors(directors);
        view.setActors(actors);
        view.setReviewCount(reviewRepository.countByMovieId(id));
        return view;
    }
}