package com.urlefy.controller;

import com.urlefy.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@RestController
public class RedirectController {

    @Autowired
    private UrlService service;

    @GetMapping("/{code}")
    @Cacheable(value = "redirects", key = "#code")
    public ResponseEntity<?> redirect(@PathVariable String code) {
        // Ignore root or empty code paths
        if (code == null || code.isBlank() || code.equalsIgnoreCase("index.html") || 
            code.equalsIgnoreCase("favicon.ico") || code.equalsIgnoreCase("robots.txt")) {
            return ResponseEntity.notFound().build();
        }

        try {
            String originalUrl = service.resolveUrl(code);
            
            // Use 301 (permanent redirect) with caching headers for better performance
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                                .location(URI.create(originalUrl))
                                .cacheControl(CacheControl.maxAge(java.time.Duration.ofDays(1)))
                                .build();
        } catch (IllegalArgumentException e) {
            String html = "<h2>404 - Short URL not found or expired</h2>";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.TEXT_HTML)
                                .cacheControl(CacheControl.maxAge(java.time.Duration.ofMinutes(5)))
                                .body(html);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Unexpected error");
        }
    }
}