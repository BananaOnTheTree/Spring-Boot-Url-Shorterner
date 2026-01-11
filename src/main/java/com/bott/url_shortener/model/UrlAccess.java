package com.bott.url_shortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "url_access",
    indexes = {
        @Index(name = "idx_short_code_access", columnList = "shortCode")
    }
)
@Getter
@Setter
public class UrlAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String shortCode;

    @Column(nullable = false)
    private Instant accessTime;

    @Column(nullable = false, length = 32)
    private String ipAddress;
}
