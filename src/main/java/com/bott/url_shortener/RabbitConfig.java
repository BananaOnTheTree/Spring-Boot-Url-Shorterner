package com.bott.url_shortener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "shorten.exchange";
    public static final String CREATE_QUEUE = "shorten.create.queue";
    public static final String CREATE_ROUTING_KEY = "shorten.create";

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public DirectExchange shortenExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue shortenCreateQueue() {
        return new Queue(CREATE_QUEUE, true);
    }

    @Bean
    public Binding shortenCreateBinding() {
        return BindingBuilder
                .bind(shortenCreateQueue())
                .to(shortenExchange())
                .with(CREATE_ROUTING_KEY);
    }
}
