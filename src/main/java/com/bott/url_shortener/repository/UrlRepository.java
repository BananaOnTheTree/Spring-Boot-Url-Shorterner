package com.bott.url_shortener.repository;

import com.bott.url_shortener.model.UrlMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<@NotNull UrlMapping, @NotNull Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);
}
