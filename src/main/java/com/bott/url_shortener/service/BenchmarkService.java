package com.bott.url_shortener.service;

import com.bott.url_shortener.exception.WriteBenchmarkException;

public interface BenchmarkService {

    long writeBenchmark(int count) throws WriteBenchmarkException;
}
