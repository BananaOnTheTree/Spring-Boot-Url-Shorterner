package com.bott.url_shortener.controller;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class UrlController {

    private static final String BASE_URL = "http://localhost:8080/";
    private UrlService urlService;
    private BenchmarkTracker tracker;

    // Do not return raw strings, as ResponseEntity has more information
    // Use ? as the generic type for future expansion, such as returning JSON objects
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String shortCode = urlService.shorten(originalUrl);
        log.info("Shortened URL: {} to short code: {}", originalUrl, shortCode);

        return ResponseEntity.ok(Map.of("shortUrl", BASE_URL + shortCode));
    }

    // New terminology: "HttpServletResponse"
    // A specialized version of HttpResponse for servlet-based applications
    // Use HttpServletResponse to handle redirection
    @GetMapping("/{shortCode}")
    public void redirectToUrl(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getByShortCode(shortCode).getOriginalUrl();
        log.info("Redirecting short code: {} to URL: {}", shortCode, originalUrl);
        response.sendRedirect(originalUrl);
    }

    @PostMapping("/benchmark/{count}")
    public String benchmark(@PathVariable int count)
            throws InterruptedException {

        tracker.start(count);

        for (int i = 0; i < count; i++) {
            urlService.shorten("https://example.com/" + i);
        }

        long time = tracker.await();

        return "Inserted " + count + " records in " + time + " ms";
    }

}
