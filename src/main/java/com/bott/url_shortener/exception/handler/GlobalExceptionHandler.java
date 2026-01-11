package com.bott.url_shortener.exception.handler;

import com.bott.url_shortener.exception.ShortCodeNotFoundException;
import com.bott.url_shortener.exception.WriteBenchmarkException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortCodeNotFoundException.class)
    public ResponseEntity<@NotNull String> handleShortCodeNotFoundException(ShortCodeNotFoundException ex) {
        log.warn("Short code not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(WriteBenchmarkException.class)
    public ResponseEntity<@NotNull String> handleWriteBenchmarkException(WriteBenchmarkException ex) {
        log.error("Benchmark write error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Benchmark write error");
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NotNull String> handleSystemError(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
    }
}
