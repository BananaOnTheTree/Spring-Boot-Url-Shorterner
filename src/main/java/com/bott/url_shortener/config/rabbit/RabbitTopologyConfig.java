package com.bott.url_shortener.config.rabbit;

import com.bott.url_shortener.messaging.exchange.Exchanges;
import com.bott.url_shortener.messaging.queue.UrlQueues;
import com.bott.url_shortener.messaging.routing.UrlRoutingKeys;
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
public class RabbitTopologyConfig {

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
        return new DirectExchange(Exchanges.EXCHANGE);
    }

    @Bean
    public Queue shortenCreateQueue() {
        return new Queue(UrlQueues.CREATE_QUEUE, true);
    }

    @Bean
    public Binding shortenCreateBinding() {
        return BindingBuilder
                .bind(shortenCreateQueue())
                .to(shortenExchange())
                .with(UrlRoutingKeys.CREATE_KEY);
    }
}
