package com.bott.url_shortener.repository;

import com.bott.url_shortener.model.UrlAccess;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlAccessRepository extends JpaRepository<@NotNull UrlAccess, @NotNull Long> {
}
