package com.cdwater.ticketbox.movie.application.query.mapper;

import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import com.cdwater.ticketbox.movie.application.query.view.GenreView;
import com.cdwater.ticketbox.movie.application.query.view.MovieListItemView;
import com.cdwater.ticketbox.movie.application.query.view.PersonView;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MoviePerson;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieViewMapper {

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
}
