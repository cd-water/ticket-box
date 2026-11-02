package com.cdwater.ticketbox.content.cinema.adapter.out.persistence;

import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.converter.HallConverter;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.po.HallPO;
import com.cdwater.ticketbox.content.cinema.domain.model.Hall;
import com.cdwater.ticketbox.content.cinema.domain.repository.HallRepository;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.mapper.HallMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HallRepositoryImpl implements HallRepository {

    private final HallMapper hallMapper;
    private final HallConverter hallConverter;

    @Override
    public Optional<Hall> findById(Long id) {
        return Optional.ofNullable(hallMapper.selectById(id)).map(hallConverter::toDomain);
    }
}
