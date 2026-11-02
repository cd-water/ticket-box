package com.cdwater.ticketbox.content.home.domain.repository;

import com.cdwater.ticketbox.content.home.domain.model.Banner;

import java.util.List;

public interface BannerRepository {

    List<Banner> findEnabled();
}
