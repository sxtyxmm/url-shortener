package com.urlefy.controller;

import com.urlefy.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
public class RedirectController {

    @Autowired
    private UrlService service;

    @GetMapping("/{code}")
    public ResponseEntity<?> redirect(@PathVariable String code) {
        // Ignore root or empty code paths (just to be safe)
        if (code == null || code.isBlank() || code.equalsIgnoreCase("index.html")) {
            return ResponseEntity.notFound().build();
        }

        try {
            String originalUrl = service.resolveUrl(code);
            return ResponseEntity.status(HttpStatus.FOUND)
                                .location(URI.create(originalUrl))
                                .build();
        } catch (IllegalArgumentException e) {
            String html = "<h2>404 - Short URL not found or expired</h2>";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .contentType(MediaType.TEXT_HTML)
                                .body(html);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Unexpected error");
        }
    }
}