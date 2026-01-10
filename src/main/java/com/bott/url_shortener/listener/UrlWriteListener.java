package com.bott.url_shortener.listener;

import com.bott.url_shortener.RabbitConfig;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.message.CreateUrlMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UrlWriteListener {

    private final UrlRepository urlRepository;

    @RabbitListener(queues = RabbitConfig.CREATE_QUEUE)
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
}
