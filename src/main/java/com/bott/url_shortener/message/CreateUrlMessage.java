package com.bott.url_shortener.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateUrlMessage implements Serializable {

    private String shortCode;
    private String originalUrl;
}
