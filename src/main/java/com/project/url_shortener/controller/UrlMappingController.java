package com.project.url_shortener.controller;

import com.project.url_shortener.controller.dto.ShortenRequest;
import com.project.url_shortener.service.UrlMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
public class UrlMappingController {

    @Autowired
    private UrlMappingService urlMappingService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody ShortenRequest request) {
        String shortUrl;
        try {
            shortUrl = urlMappingService.shortenUrl(request.getOriginalUrl());
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        String originalUrl = urlMappingService.getOriginalUrl(shortUrl);
        return originalUrl != null ? ResponseEntity.status(302).location(URI.create(originalUrl)).build() 
                                   : ResponseEntity.notFound().build();
    }
}
