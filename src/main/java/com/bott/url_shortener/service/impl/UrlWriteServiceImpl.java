package com.bott.url_shortener.service.impl;

import com.bott.url_shortener.RabbitConfig;
import com.bott.url_shortener.message.CreateUrlMessage;
import com.bott.url_shortener.service.UrlWriteService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UrlWriteServiceImpl implements UrlWriteService {

    private final RabbitTemplate rabbitTemplate;

    private String generateNewShortCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    @Override
    public String shorten(String originalUrl) {
        String shortCode = generateNewShortCode();

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.CREATE_ROUTING_KEY,
                new CreateUrlMessage(shortCode, originalUrl)
        );

        return shortCode;
    }
}
