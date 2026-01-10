package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.exception.ShortCodeNotFoundException;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.service.UrlReadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlReadServiceImpl implements UrlReadService {

    private final UrlRepository urlRepository;

    @Override
    public UrlMapping getByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow( () -> new ShortCodeNotFoundException("Short code not found: " + shortCode) );
    }
}
