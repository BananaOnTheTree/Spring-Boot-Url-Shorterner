package com.bott.url_shortener;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class BenchmarkTracker {

    private CountDownLatch latch;
    private long startTime;

    public synchronized void start(int count) {
        this.latch = new CountDownLatch(count);
        this.startTime = System.currentTimeMillis();
    }

    public void markDone(int processed) {
        for (int i = 0; i < processed; i++) {
            latch.countDown();
        }
    }

    public long await() throws InterruptedException {
        latch.await();
        return System.currentTimeMillis() - startTime;
    }
}
