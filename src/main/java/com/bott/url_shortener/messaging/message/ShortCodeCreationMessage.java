package com.bott.url_shortener.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ShortCodeCreationMessage implements Serializable {

    private String shortCode;
    private String originalUrl;
}
