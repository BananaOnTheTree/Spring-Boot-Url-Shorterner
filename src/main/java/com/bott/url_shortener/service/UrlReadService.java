package com.bott.url_shortener.service;

import com.bott.url_shortener.model.UrlMapping;

public interface UrlReadService {

    UrlMapping getByShortCode(String shortCode);
}
