package com.bott.url_shortener.messaging.message;

public record CodeCreationMessage (String shortCode, String originalUrl) {}
