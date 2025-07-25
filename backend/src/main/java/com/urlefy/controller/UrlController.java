package com.urlefy.controller;

import com.urlefy.service.UrlService;
import com.urlefy.model.UrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody UrlRequest req) {
        String code = service.shortenUrl(req.getUrl(), Optional.ofNullable(req.getCustomAlias()));
        return ResponseEntity.ok("http://localhost:8080/" + code);
    }
}