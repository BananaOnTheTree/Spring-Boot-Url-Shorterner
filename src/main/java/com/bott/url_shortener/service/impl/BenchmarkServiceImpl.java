package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.exception.WriteBenchmarkException;
import com.bott.url_shortener.service.BenchmarkService;
import com.bott.url_shortener.service.UrlWriteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private UrlWriteService writeService;
    private BenchmarkTracker tracker;

    @Override
    public long writeBenchmark(int count) throws WriteBenchmarkException {
        try {
            tracker.start(count);

            for (int i = 0; i < count; i++) {
                writeService.shorten("https://example.com/" + i);
            }

            return tracker.await();
        } catch (Exception e) {
            throw new WriteBenchmarkException(e.getMessage());
        }
    }
}
