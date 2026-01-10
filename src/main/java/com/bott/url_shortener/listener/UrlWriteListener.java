package com.bott.url_shortener.listener;

import com.bott.url_shortener.config.RabbitConfig;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.message.CreateUrlMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class UrlWriteListener {

    private final UrlRepository urlRepository;

//    @RabbitListener(queues = RabbitConfig.CREATE_QUEUE)
    public void addUrlMapping(CreateUrlMessage message) {
        log.info("Consuming CreateUrlMessage: {}", message);
        if (urlRepository.existsByShortCode(message.getShortCode())) {
            return;
        }

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortCode(message.getShortCode());
        urlMapping.setOriginalUrl(message.getOriginalUrl());

        urlRepository.save(urlMapping);
    }

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
    }
}
