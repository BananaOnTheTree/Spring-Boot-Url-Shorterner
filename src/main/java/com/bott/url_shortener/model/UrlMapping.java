package com.bott.url_shortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "url_mapping",
    indexes = {
        @Index(name = "idx_short_code", columnList = "shortCode")
    }
)
@Getter
@Setter
public class UrlMapping {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "url_seq"
    )
    @SequenceGenerator(
            name = "url_seq",
            sequenceName = "url_seq"
    )
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String shortCode;

    @Column(nullable = false, length = 2048)
    private String originalUrl;
}
