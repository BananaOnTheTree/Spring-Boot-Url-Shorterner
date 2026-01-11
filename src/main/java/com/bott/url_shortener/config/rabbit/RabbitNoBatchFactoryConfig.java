package com.bott.url_shortener.config.rabbit;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("nobatch")
public class RabbitNoBatchFactoryConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory noBatchFactory(
            ConnectionFactory connectionFactory,
            JacksonJsonMessageConverter jsonMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);

        factory.setBatchListener(false);
        factory.setConsumerBatchEnabled(false);

        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(true);

        return factory;
    }
}
