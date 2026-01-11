package com.bott.url_shortener;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class BenchmarkTracker {

    private CountDownLatch latch;
    private long startTime;
    private boolean isEnable = false;

    public synchronized void start(int count) {
        this.latch = new CountDownLatch(count);
        this.startTime = System.currentTimeMillis();
        this.isEnable = true;
    }

    public void markDone(int processed) {
        for (int i = 0; i < processed; i++) {
            latch.countDown();
        }
    }

    public long await() throws InterruptedException {
        latch.await();
        this.isEnable = false;
        return System.currentTimeMillis() - startTime;
    }
}