package com.opencbs.core.repositories;

import com.opencbs.core.domain.GlobalSettings;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalSettingsRepository  extends JpaRepository<GlobalSettings, String> {
    Optional<GlobalSettings> findByName(String name);
}
