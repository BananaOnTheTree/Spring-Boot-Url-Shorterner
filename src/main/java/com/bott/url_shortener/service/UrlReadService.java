package com.bott.url_shortener.service;

import com.bott.url_shortener.model.UrlMapping;

import java.time.Instant;

public interface UrlReadService {

    UrlMapping getByShortCode(String shortCode);
    void publishUrlViewEvent(String shortCode, String ipAddress, Instant time);
}
