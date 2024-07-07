package com.project.url_shortener.service;

import com.project.url_shortener.model.UrlMapping;
import com.project.url_shortener.repository.UrlMappingRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlMappingService {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    private static String generateShortUrl(String longUrl) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(longUrl.getBytes());
        byte[] digest = md.digest();

        // Convert digest to a Base62 encoded string
        return encodeBase62(digest).substring(0, 7); // Using first 7 characters of Base62 encoded hash = 3521614606208 valid short url possibilities (62^7)
    }

    private static String encodeBase62(byte[] input) {
        StringBuilder encoded = new StringBuilder();
        for (byte b : input) {
            int value = b & 0xFF; // Converts the byte to an unsigned int (0 to 255)
            do {
                encoded.append(BASE62.charAt(value % 62)); // Maps the value to a Base62 character
                value /= 62; // Reduces the value by dividing it by 62
            } while (value > 0);
        }
        return encoded.toString();
    }

    public String shortenUrl(String originalUrl) throws NoSuchAlgorithmException {
        // Using a hashed version of the original url instead of the original url avoids problems in Redis keys parsing
        String originalUrlHash = hashUrl(originalUrl);

        UrlMapping existingMapping = readFromRedisByOriginalHash(originalUrlHash);
        if (existingMapping != null) {
            return existingMapping.getShortUrl();
        }

        String shortUrl = generateShortUrl(originalUrl);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setHash(originalUrlHash);
        writeToRedis(urlMapping);
        return shortUrl;
    }

    private UrlMapping readFromRedisByOriginalHash(String originalUrlHash) {
        return urlMappingRepository.findByHash(originalUrlHash);
    }

    private UrlMapping readFromRedisByShortUrl(String shortUrl) {
        return urlMappingRepository.findByShortUrl(shortUrl);
       // return (UrlMapping) redisTemplate.opsForHash().get("UrlMapping", shortUrl);
    }

    private void writeToRedis(UrlMapping urlMapping) {
        urlMappingRepository.save(urlMapping);
    }

    private String hashUrl(String originalUrl) {
        return DigestUtils.sha256Hex(originalUrl);
    }

    public String getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = readFromRedisByShortUrl(shortUrl);
        return urlMapping != null ? urlMapping.getOriginalUrl() : null;
    }
}
