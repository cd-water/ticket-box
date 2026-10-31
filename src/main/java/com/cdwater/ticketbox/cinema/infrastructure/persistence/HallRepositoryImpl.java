package com.cdwater.ticketbox.cinema.infrastructure.persistence;

import com.cdwater.ticketbox.cinema.domain.model.Hall;
import com.cdwater.ticketbox.cinema.domain.repository.HallRepository;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.mapper.HallMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HallRepositoryImpl implements HallRepository {

    private final HallMapper hallMapper;

    @Override
    public Optional<Hall> findById(Long id) {
        return Optional.ofNullable(hallMapper.selectById(id));
    }
}
