package com.bott.url_shortener.listener.code_creation;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.messaging.message.CodeCreationMessage;
import com.bott.url_shortener.messaging.queue.UrlQueues;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor
@Profile("batch")
@Slf4j
public class UrlBatchWriteListener {

    private final UrlRepository urlRepository;
    private final BenchmarkTracker tracker;

    @RabbitListener(
            queues = UrlQueues.CREATE_QUEUE,
            containerFactory = "codeCreationBatchFactory"
    )
    @Transactional
    public void addUrlMappingBatch(List<CodeCreationMessage> messages) {
        List<UrlMapping> entities = messages.stream().map(m -> {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setShortCode(m.shortCode());
            urlMapping.setOriginalUrl(m.originalUrl());
            return urlMapping;
        }).toList();

        urlRepository.saveAll(entities);
        if (tracker.isEnabled()) {
            tracker.markDone(messages.size());
        }
        log.info("Successfully shortened {} URLs in batch.", messages.size());
        if (!messages.isEmpty()) {
            log.info("First record: short code: {}, original URL: {}", messages.get(0).shortCode(), messages.get(0).originalUrl());
        }
    }
}
