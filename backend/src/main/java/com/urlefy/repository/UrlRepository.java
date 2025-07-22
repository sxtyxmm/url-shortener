package com.urlefy.repository;

import com.urlefy.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;
import java.util.List;

public interface UrlRepository extends MongoRepository<ShortUrl, String> {
    
    @Cacheable(value = "urls", key = "#shortCode")
    Optional<ShortUrl> findByShortCode(String shortCode);
    
    @Query(value = "{}", sort = "{ 'clickCount': -1 }")
    List<ShortUrl> findTop1000ByOrderByClickCountDesc();
    
    @Query(value = "{ 'shortCode': ?0 }", fields = "{ 'originalUrl': 1 }")
    Optional<ShortUrl> findOriginalUrlByShortCode(String shortCode);
    
    @Query(value = "{ 'expiryDate': { $gt: ?#{new java.util.Date()} } }", sort = "{ 'clickCount': -1 }")
    Page<ShortUrl> findActiveUrlsByPopularity(Pageable pageable);
}
