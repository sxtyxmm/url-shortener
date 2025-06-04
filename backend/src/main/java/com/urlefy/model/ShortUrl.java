package com.urlefy.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "urls")
public class ShortUrl {

    @Id
    private String id;
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiry;
    private Long clickCount;

    public ShortUrl(String shortCode, String originalUrl, LocalDateTime createdAt, LocalDateTime expiry, Long clickCount) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiry = expiry;
        this.clickCount = clickCount;
    }
}
