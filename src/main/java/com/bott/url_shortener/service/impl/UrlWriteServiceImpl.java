package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.messaging.exchange.Exchanges;
import com.bott.url_shortener.messaging.message.CodeCreationMessage;
import com.bott.url_shortener.messaging.routing.UrlRoutingKeys;
import com.bott.url_shortener.service.UrlWriteService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlWriteServiceImpl implements UrlWriteService {

    private final RabbitTemplate rabbitTemplate;

    @NotNull
    private String generateNewShortCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public String shorten(String originalUrl) {
        String shortCode = generateNewShortCode();

        rabbitTemplate.convertAndSend(
                Exchanges.EXCHANGE,
                UrlRoutingKeys.CREATE_KEY,
                new CodeCreationMessage(shortCode, originalUrl)
        );

        return shortCode;
    }
}
