package com.bott.url_shortener.config.redis;

import com.bott.url_shortener.model.UrlMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public JacksonJsonRedisSerializer<@NotNull UrlMapping> urlMappingSerializer(ObjectMapper om) {
        return new JacksonJsonRedisSerializer<>(om, UrlMapping.class);
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper
    ) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        RedisCacheConfiguration baseConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(keySerializer)
                        );

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration(
                        "urlMappings",
                        baseConfig
                                .serializeValuesWith(
                                        RedisSerializationContext.SerializationPair.fromSerializer(
                                                urlMappingSerializer(objectMapper)
                                        )
                                )
                                .entryTtl(Duration.ofHours(1))
                )
                .build();
    }
}


