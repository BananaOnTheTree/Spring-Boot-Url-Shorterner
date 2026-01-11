package com.bott.url_shortener.listener.code_view;

import com.bott.url_shortener.messaging.message.CodeViewMessage;
import com.bott.url_shortener.messaging.queue.UrlQueues;
import com.bott.url_shortener.model.UrlAccess;
import com.bott.url_shortener.repository.UrlAccessRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UrlViewListener {

    private final UrlAccessRepository urlAccessRepository;

    @RabbitListener(
            queues = UrlQueues.VIEW_QUEUE,
            containerFactory = "codeViewFactory"
    )
    @Transactional
    public void viewUrl(CodeViewMessage message) {
        UrlAccess urlAccess = new UrlAccess();
        urlAccess.setShortCode(message.shortCode());
        urlAccess.setIpAddress(message.ipAddress());
        urlAccess.setAccessTime(message.time());

        urlAccessRepository.save(urlAccess);
        log.info("Recorded access for short code: {} from IP: {}", message.shortCode(), message.ipAddress());
    }
}
