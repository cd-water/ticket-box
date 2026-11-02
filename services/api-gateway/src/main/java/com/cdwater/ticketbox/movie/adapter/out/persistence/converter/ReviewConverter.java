package com.cdwater.ticketbox.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.movie.adapter.out.persistence.po.ReviewPO;
import com.cdwater.ticketbox.movie.domain.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewConverter {

    Review toDomain(ReviewPO po);

    List<Review> toDomain(List<ReviewPO> poList);

    ReviewPO toPO(Review domain);
}
