package com.urlefy.controller;

import com.urlefy.service.UrlService;
import com.urlefy.model.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@Validated
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody UrlRequest req) {
        try {
            String code = service.shortenUrl(req.getUrl(), Optional.ofNullable(req.getCustomAlias()));
            
            // Return response with caching headers
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(java.time.Duration.ofMinutes(5)))
                    .body("http://localhost:8080/" + code);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing request");
        }
    }
    
    @GetMapping("/stats/{code}")
    public ResponseEntity<?> getStats(@PathVariable String code) {
        try {
            // This would return statistics about the shortened URL
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(java.time.Duration.ofMinutes(1)))
                    .body("Statistics endpoint - to be implemented");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}