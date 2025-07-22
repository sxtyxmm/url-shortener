package com.urlefy.service;

import com.urlefy.model.ShortUrl;
import com.urlefy.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepo;

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    // In-memory cache for ultra-fast lookups
    private final ConcurrentHashMap<String, String> localCache = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    @CachePut(value = "urls", key = "#code")
    public String shortenUrl(String originalUrl, Optional<String> customCode) {
        String code = customCode.orElse(generateOptimizedShortCode());
        
        if (!isValidCode(code)) {
            throw new IllegalArgumentException("Invalid custom alias provided");
        }
        
        // Store in local cache immediately for ultra-fast access
        localCache.put(code, originalUrl);
        
        // Async database write for better response time
        asyncSaveToDatabase(code, originalUrl);
        
        // Store in Redis with pipeline for better performance
        redisTemplate.opsForValue().set(code, originalUrl, Duration.ofDays(30));
        
        return code;
    }

    @Async
    public CompletableFuture<Void> asyncSaveToDatabase(String code, String originalUrl) {
        try {
            ShortUrl url = new ShortUrl(code, originalUrl, LocalDateTime.now(), LocalDateTime.now().plusDays(30), 0L);
            urlRepo.save(url);
        } catch (Exception e) {
            // Log error but don't block the main flow
            System.err.println("Error saving to database: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    @Cacheable(value = "urls", key = "#code")
    public String resolveUrl(String code) {
        // Level 1: Local cache (fastest)
        String url = localCache.get(code);
        if (url != null) {
            return url;
        }
        
        // Level 2: Redis cache
        url = redisTemplate.opsForValue().get(code);
        if (url != null) {
            // Store in local cache for next time
            localCache.put(code, url);
            return url;
        }
        
        // Level 3: Database (slowest)
        ShortUrl found = urlRepo.findByShortCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Short URL not found"));
        
        String originalUrl = found.getOriginalUrl();
        
        // Update both caches
        localCache.put(code, originalUrl);
        redisTemplate.opsForValue().set(code, originalUrl, Duration.ofDays(30));
        
        // Async increment click count
        asyncIncrementClickCount(code);
        
        return originalUrl;
    }
    
    @Async
    public CompletableFuture<Void> asyncIncrementClickCount(String code) {
        try {
            urlRepo.findByShortCode(code).ifPresent(shortUrl -> {
                shortUrl.setClickCount(shortUrl.getClickCount() + 1);
                urlRepo.save(shortUrl);
            });
        } catch (Exception e) {
            System.err.println("Error incrementing click count: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    // Ultra-fast short code generation using base62 encoding
    private String generateOptimizedShortCode() {
        long timestamp = counter.incrementAndGet();
        StringBuilder result = new StringBuilder();
        
        // Convert to base62 for shorter codes
        while (timestamp > 0) {
            result.append(CHARSET.charAt((int) (timestamp % 62)));
            timestamp /= 62;
        }
        
        // Add randomness to prevent collisions
        for (int i = 0; i < 2; i++) {
            result.append(CHARSET.charAt(random.nextInt(62)));
        }
        
        return result.reverse().toString();
    }
    
    private boolean isValidCode(String code) {
        return code != null && code.length() >= 3 && code.length() <= 12 && 
               code.matches("^[a-zA-Z0-9_-]+$");
    }
    
    // Method to warm up cache with popular URLs
    public void warmUpCache() {
        // This could be called on startup to pre-load popular URLs
        CompletableFuture.runAsync(() -> {
            try {
                urlRepo.findTop1000ByOrderByClickCountDesc().forEach(url -> {
                    localCache.put(url.getShortCode(), url.getOriginalUrl());
                    redisTemplate.opsForValue().set(url.getShortCode(), url.getOriginalUrl(), Duration.ofDays(30));
                });
            } catch (Exception e) {
                System.err.println("Error warming up cache: " + e.getMessage());
            }
        });
    }
}
