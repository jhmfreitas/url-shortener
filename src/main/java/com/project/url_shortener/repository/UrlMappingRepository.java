package com.project.url_shortener.repository;

import com.project.url_shortener.model.UrlMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlMappingRepository extends MongoRepository<UrlMapping, String> {
    UrlMapping findByHash(String hash);
    UrlMapping findByShortUrl(String shortUrl);
}
