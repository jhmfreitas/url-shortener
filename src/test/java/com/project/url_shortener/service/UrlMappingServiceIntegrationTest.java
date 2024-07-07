package com.project.url_shortener.service;

import com.project.url_shortener.model.UrlMapping;
import com.project.url_shortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UrlMappingServiceIntegrationTest {

    @Autowired
    private UrlMappingService urlMappingService;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @BeforeEach
    void setUp() {
        urlMappingRepository.deleteAll();
    }

    @Test
    public void testShortenUrl_NewUrl() throws NoSuchAlgorithmException {
        String originalUrl = "http://example.com";

        String shortUrl = urlMappingService.shortenUrl(originalUrl);

        assertNotNull(shortUrl);
        assertEquals(7, shortUrl.length());

        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        assertNotNull(urlMapping);
        assertEquals(originalUrl, urlMapping.getOriginalUrl());
    }

    @Test
    public void testShortenUrl_ExistingUrl() throws NoSuchAlgorithmException {
        String originalUrl = "http://example.com";
        String shortUrl = "abc1234";

        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setOriginalUrl(originalUrl);
        existingMapping.setShortUrl(shortUrl);
        existingMapping.setHash(urlMappingService.hashUrl(originalUrl));
        urlMappingRepository.save(existingMapping);

        String result = urlMappingService.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(shortUrl, result);
    }

    @Test
    public void testGetOriginalUrl_ExistingShortUrl() {
        String shortUrl = "abc1234";
        String originalUrl = "http://example.com";

        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setOriginalUrl(originalUrl);
        existingMapping.setShortUrl(shortUrl);
        urlMappingRepository.save(existingMapping);

        String result = urlMappingService.getOriginalUrl(shortUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result);
    }

    @Test
    public void testGetOriginalUrl_NonExistingShortUrl() {
        String shortUrl = "abc1234";

        String result = urlMappingService.getOriginalUrl(shortUrl);

        assertNull(result);
    }
}
