package com.bott.url_shortener.listener.code_creation;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.messaging.queue.UrlQueues;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.messaging.message.CodeCreationMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Profile("nobatch")
@Slf4j
public class UrlWriteListener {

    private final UrlRepository urlRepository;
    private final BenchmarkTracker tracker;

    @RabbitListener(
            queues = UrlQueues.CREATE_QUEUE,
            containerFactory = "codeCreationNoBatchFactory"
    )
    @Transactional
    public void addUrlMapping(CodeCreationMessage message) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortCode(message.shortCode());
        urlMapping.setOriginalUrl(message.originalUrl());

        urlRepository.save(urlMapping);
        log.info("Shortened URL: {} to short code: {}", message.originalUrl(), message.shortCode());
        if (tracker.isEnable()) {
            tracker.markDone(1);
        }
    }
}
