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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class UrlReadServiceImpl implements UrlReadService {

    private static final String CACHE_PREFIX = "url_mapping::";
    private final UrlRepository urlRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, UrlMapping> redisTemplate;

    @Override
    public UrlMapping getByShortCode(String shortCode) {
        String cacheKey = CACHE_PREFIX + shortCode;

        long start = System.nanoTime();

        UrlMapping urlMapping = redisTemplate.opsForValue().get(cacheKey);

        long afterRedis = System.nanoTime();
        if (urlMapping != null) {
            log.info("Cache hit for short code: {}, time to get from Redis: {} ms",
                    shortCode,
                    (afterRedis - start) / 1_000_000);
            return urlMapping;
        }

        log.info("Cache miss for short code: {}", shortCode);

        urlMapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortCodeNotFoundException("Short code not found: " + shortCode));

        redisTemplate.opsForValue().set(cacheKey, urlMapping, Duration.ofHours(1));

        long end = System.nanoTime();

        log.info("Total time getByShortCode({}) = {} ms",
                shortCode,
                (end - start) / 1_000_000);

        return urlMapping;
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
