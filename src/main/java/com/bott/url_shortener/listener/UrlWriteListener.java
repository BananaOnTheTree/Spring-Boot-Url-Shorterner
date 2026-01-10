package com.bott.url_shortener.listener;

import com.bott.url_shortener.BenchmarkTracker;
import com.bott.url_shortener.config.RabbitConfig;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.message.CreateUrlMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Profile("nobatch")
@Slf4j
public class UrlWriteListener {

    private final UrlRepository urlRepository;
    private final BenchmarkTracker tracker;

    @RabbitListener(
            queues = RabbitConfig.CREATE_QUEUE
    )
    @Transactional
    public void addUrlMapping(CreateUrlMessage message) {

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortCode(message.getShortCode());
        urlMapping.setOriginalUrl(message.getOriginalUrl());

        urlRepository.save(urlMapping);
        tracker.markDone(1);
    }

}
