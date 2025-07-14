package com.urlefy.service;

import com.urlefy.model.ShortUrl;
import com.urlefy.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepo;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String shortenUrl(String originalUrl, Optional<String> customCode) {
        String code = customCode.orElse(generateShortCode());
        if (!isValidCode(code)) {
            throw new IllegalArgumentException("Invalid custom alias provided");
        }
        System.out.println("Generated short code: " + code);
        ShortUrl url = new ShortUrl(code, originalUrl, LocalDateTime.now(), LocalDateTime.now().plusDays(7), 0L);
        urlRepo.save(url);
        redisTemplate.opsForValue().set(code, originalUrl, Duration.ofDays(7));
        return code;
    }

    public String resolveUrl(String code) {
        System.out.println("Looking up code: " + code);
        String url = redisTemplate.opsForValue().get(code);
        if (url != null) {
            System.out.println("Found in Redis: " + url);
            return url;
        }
        ShortUrl found = urlRepo.findByShortCode(code).orElseThrow(() -> new IllegalArgumentException("Short URL not found"));
        redisTemplate.opsForValue().set(code, found.getOriginalUrl(), Duration.ofDays(7));
        return found.getOriginalUrl();
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
    private boolean isValidCode(String code) {
        return code.matches("^[a-zA-Z0-9_-]+$");
    }
}
