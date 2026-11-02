package com.cdwater.ticketbox.content.movie.application.query.converter;

import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.GenreDTO;
import com.cdwater.ticketbox.content.api.dto.MovieDetailDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import com.cdwater.ticketbox.content.api.dto.PersonDTO;
import com.cdwater.ticketbox.content.movie.domain.model.Genre;
import com.cdwater.ticketbox.content.movie.domain.model.Movie;
import com.cdwater.ticketbox.content.movie.domain.model.MoviePerson;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieViewConverter {

    MovieListItemDTO toListItemView(Movie movie);

    MovieBriefDTO toBrief(Movie movie);

    GenreDTO toView(Genre genre);

    @Mapping(target = "name", source = "personName")
    @Mapping(target = "avatar", source = "personAvatar")
    @Mapping(target = "roleName", ignore = true)
    PersonDTO toView(MoviePerson person);

    @AfterMapping
    default void applyRole(@MappingTarget PersonDTO view, MoviePerson person) {
        view.setRoleName(person.isActor() ? person.getRoleName() : null);
    }
    @Mapping(target = "tab", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "directors", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    MovieDetailDTO toDetailView(Movie movie);
}