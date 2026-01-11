package com.bott.url_shortener.controller;

import com.bott.url_shortener.service.BenchmarkService;
import com.bott.url_shortener.service.UrlReadService;
import com.bott.url_shortener.service.UrlWriteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class UrlController {

    private static final String BASE_URL = "http://localhost:8080/";
    private UrlWriteService writeService;
    private UrlReadService readService;
    private BenchmarkService benchmarkService;

    // Do not return raw strings, as ResponseEntity has more information
    @PostMapping("/shorten")
    public ResponseEntity<@NotNull Map<String, String>> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String shortCode = writeService.shorten(originalUrl);

        return ResponseEntity.ok(Map.of("shortUrl", BASE_URL + shortCode));
    }

    // New terminology: "HttpServletResponse"
    // A specialized version of HttpResponse for servlet-based applications
    // Use HttpServletResponse to handle redirection
    @GetMapping("/{shortCode}")
    public void redirectToUrl(@PathVariable String shortCode, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originalUrl = readService.getByShortCode(shortCode).getOriginalUrl();
        log.info("Redirecting short code: {} to URL: {}", shortCode, originalUrl);
        readService.publishUrlViewEvent(shortCode, request.getRemoteAddr(), Instant.now());
        response.sendRedirect(originalUrl);
    }

    @PostMapping("/benchmark/{count}")
    public ResponseEntity<@NotNull String> benchmark(@PathVariable int count) {
        long result = benchmarkService.writeBenchmark(count);
        String message = "Benchmark completed: " + count + " URLs shortened in " + result + " ms.";
        log.info(message);
        return ResponseEntity.ok(message);
    }
}
