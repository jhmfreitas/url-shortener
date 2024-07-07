package com.project.url_shortener.service;

import com.project.url_shortener.model.UrlMapping;
import com.project.url_shortener.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UrlMappingServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @InjectMocks
    private UrlMappingService urlMappingService;

    @Test
    public void testShortenUrl_NewUrl() throws NoSuchAlgorithmException {
        String originalUrl = "http://example.com";
        String originalUrlHash = urlMappingService.hashUrl(originalUrl);

        when(urlMappingRepository.findByHash(anyString())).thenReturn(null);
        when(urlMappingRepository.save(any(UrlMapping.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = urlMappingService.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(7, result.length());
        verify(urlMappingRepository, times(1)).findByHash(originalUrlHash);
        verify(urlMappingRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    public void testShortenUrl_ExistingUrl() throws NoSuchAlgorithmException {
        String originalUrl = "http://example.com";
        String shortUrl = "abc1234";
        String originalUrlHash = urlMappingService.hashUrl(originalUrl);

        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setOriginalUrl(originalUrl);
        existingMapping.setShortUrl(shortUrl);
        existingMapping.setHash(originalUrlHash);

        when(urlMappingRepository.findByHash(anyString())).thenReturn(existingMapping);

        String result = urlMappingService.shortenUrl(originalUrl);

        assertNotNull(result);
        assertEquals(shortUrl, result);
        verify(urlMappingRepository, times(1)).findByHash(originalUrlHash);
        verify(urlMappingRepository, never()).save(any(UrlMapping.class));
    }

    @Test
    public void testGetOriginalUrl_ExistingShortUrl() {
        String shortUrl = "abc1234";
        String originalUrl = "http://example.com";

        UrlMapping existingMapping = new UrlMapping();
        existingMapping.setOriginalUrl(originalUrl);
        existingMapping.setShortUrl(shortUrl);

        when(urlMappingRepository.findByShortUrl(anyString())).thenReturn(existingMapping);

        String result = urlMappingService.getOriginalUrl(shortUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result);
        verify(urlMappingRepository, times(1)).findByShortUrl(shortUrl);
    }

    @Test
    public void testGetOriginalUrl_NonExistingShortUrl() {
        String shortUrl = "abc1234";

        when(urlMappingRepository.findByShortUrl(anyString())).thenReturn(null);

        String result = urlMappingService.getOriginalUrl(shortUrl);

        assertNull(result);
        verify(urlMappingRepository, times(1)).findByShortUrl(shortUrl);
    }
}
