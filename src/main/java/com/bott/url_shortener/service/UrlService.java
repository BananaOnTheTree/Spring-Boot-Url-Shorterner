package com.bott.url_shortener.service;

import com.bott.url_shortener.model.UrlMapping;

public interface UrlService {

    String shorten(String originalUrl);

    UrlMapping getByShortCode(String shortCode);
}
