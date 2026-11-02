package com.cdwater.ticketbox.admin.domain.repository;

import com.cdwater.ticketbox.admin.domain.model.Admin;

import java.util.Optional;

public interface AdminRepository {

    Optional<Admin> findByUsername(String username);

    Optional<Admin> findById(Long id);
}
