package com.cdwater.ticketbox.movie.application.query.converter;

import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import com.cdwater.ticketbox.movie.application.query.view.GenreView;
import com.cdwater.ticketbox.movie.application.query.view.MovieDetailView;
import com.cdwater.ticketbox.movie.application.query.view.MovieListItemView;
import com.cdwater.ticketbox.movie.application.query.view.PersonView;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MoviePerson;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieViewConverter {

    MovieListItemView toListItemView(Movie movie);

    MovieBrief toBrief(Movie movie);

    GenreView toView(Genre genre);

    @Mapping(target = "name", source = "personName")
    @Mapping(target = "avatar", source = "personAvatar")
    @Mapping(target = "roleName", ignore = true)
    PersonView toView(MoviePerson person);

    @AfterMapping
    default void applyRole(@MappingTarget PersonView view, MoviePerson person) {
        view.setRoleName(person.isActor() ? person.getRoleName() : null);
    }
    @Mapping(target = "tab", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    MovieDetailView toDetailView(Movie movie);
}
