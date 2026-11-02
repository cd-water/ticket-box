package com.cdwater.ticketbox.home.domain.repository;

import com.cdwater.ticketbox.home.domain.model.Banner;

import java.util.List;

public interface BannerRepository {

    List<Banner> findEnabled();
}
