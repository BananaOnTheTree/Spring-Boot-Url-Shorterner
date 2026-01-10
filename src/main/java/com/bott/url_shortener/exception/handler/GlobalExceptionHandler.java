package com.bott.url_shortener.exception.handler;

import com.bott.url_shortener.exception.ShortCodeNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortCodeNotFoundException.class)
    public void handleShortCodeNotFoundException(ShortCodeNotFoundException ex, HttpServletResponse response) throws IOException {
        log.warn("Short code not found: {}", ex.getMessage());
        response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSystemError(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(500).body("Internal server error");
    }
}
