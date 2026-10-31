package com.cdwater.ticketbox.movie.application.query;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.error.ResultCode;
import com.cdwater.ticketbox.movie.application.query.mapper.MovieViewMapper;
import com.cdwater.ticketbox.movie.application.query.view.CrewView;
import com.cdwater.ticketbox.movie.application.query.view.MovieDetailView;
import com.cdwater.ticketbox.movie.application.query.view.MovieListItemView;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.repository.MovieRepository;
import com.cdwater.ticketbox.movie.domain.repository.ReviewRepository;
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
    private final MovieViewMapper movieViewMapper;

    public PageResult<MovieListItemView> list(MovieListQuery query) {
        PageResult<Movie> page = movieRepository.page(
                query.getTab(), query.getKeyword(), query.getPage(), query.getSize());
        List<MovieListItemView> records = page.getRecords().stream()
                .map(movieViewMapper::toListItemView)
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public MovieDetailView detail(Long id) {
        Movie movie = movieRepository.findById(id)
                .filter(Movie::isOnShelf)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));

        MovieDetailView view = new MovieDetailView();
        view.setId(movie.getId());
        view.setTitle(movie.getTitle());
        view.setPoster(movie.getPoster());
        view.setSynopsis(movie.getSynopsis());
        view.setDuration(movie.getDuration());
        view.setReleaseDate(movie.getReleaseDate());
        view.setTab(movie.isUpcoming(LocalDateTime.now()) ? "upcoming" : "showing");
        view.setGenres(movieRepository.findGenres(id).stream()
                .map(movieViewMapper::toView)
                .toList());

        List<CrewView> directors = new ArrayList<>();
        List<CrewView> actors = new ArrayList<>();
        movieRepository.findCrew(id).forEach(crew -> {
            if (crew.isDirector()) {
                directors.add(movieViewMapper.toView(crew));
            } else if (crew.isActor()) {
                actors.add(movieViewMapper.toView(crew));
            }
        });
        view.setDirectors(directors);
        view.setActors(actors);
        view.setReviewCount(reviewRepository.countByMovieId(id));
        return view;
    }
}
