package com.bott.url_shortener.listener;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.config.RabbitConfig;
import com.bott.url_shortener.message.CreateUrlMessage;
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
            queues = RabbitConfig.CREATE_QUEUE,
            containerFactory = "batchFactory"
    )
    @Transactional
    public void addUrlMappingBatch(List<CreateUrlMessage> messages) {
        log.info("Consuming {} CreateUrlMessages", messages.size());

        List<UrlMapping> entities = messages.stream().map(m -> {
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setShortCode(m.getShortCode());
            urlMapping.setOriginalUrl(m.getOriginalUrl());
            return urlMapping;
        }).toList();

        urlRepository.saveAll(entities);
        tracker.markDone(messages.size());
    }
}
