package com.bott.url_shortener;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class BenchmarkTracker {

    private CountDownLatch latch;
    private long startTime;
    private boolean isEnabled = false;

    public synchronized void start(int count) {
        this.latch = new CountDownLatch(count);
        this.startTime = System.currentTimeMillis();
        this.isEnabled = true;
    }

    public void markDone(int processed) {
        for (int i = 0; i < processed; i++) {
            latch.countDown();
        }
    }

    public long await() throws InterruptedException {
        latch.await();
        this.isEnabled = false;
        return System.currentTimeMillis() - startTime;
    }
}