package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.service.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final BenchmarkTracker tracker;

    private String generateNewShortCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public String shorten(String originalUrl) {
        String shortCode = generateNewShortCode();
        UrlMapping mapping = new UrlMapping();

        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(shortCode);

        urlRepository.save(mapping);
        tracker.markDone(1);
        return shortCode;
    }

    @Override
    public UrlMapping getByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow( () -> new RuntimeException("Short code not found") );
    }
}
