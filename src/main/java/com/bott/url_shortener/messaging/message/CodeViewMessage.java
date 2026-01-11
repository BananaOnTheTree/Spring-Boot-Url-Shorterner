package com.bott.url_shortener.messaging.message;


import java.time.Instant;

public record CodeViewMessage (String shortCode, String ipAddress, Instant time) {}