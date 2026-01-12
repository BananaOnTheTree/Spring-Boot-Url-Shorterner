package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.exception.ShortCodeNotFoundException;
import com.bott.url_shortener.messaging.exchange.Exchanges;
import com.bott.url_shortener.messaging.message.CodeViewMessage;
import com.bott.url_shortener.messaging.routing.UrlRoutingKeys;
import com.bott.url_shortener.model.UrlMapping;
import com.bott.url_shortener.repository.UrlRepository;
import com.bott.url_shortener.service.UrlReadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class UrlReadServiceImpl implements UrlReadService {

    private final UrlRepository urlRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Cacheable(
            value = "urlMappings",
            key = "#shortCode",
            unless = "#result == null"
    )
    public UrlMapping getByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new ShortCodeNotFoundException("Short code not found: " + shortCode)
                );
    }


    @Override
    public void publishUrlViewEvent(String shortCode, String ipAddress, Instant time) {
        CodeViewMessage message = new CodeViewMessage(shortCode, ipAddress, time);
        rabbitTemplate.convertAndSend(
                Exchanges.EXCHANGE,
                UrlRoutingKeys.VIEW_KEY,
                message
        );
    }
}
