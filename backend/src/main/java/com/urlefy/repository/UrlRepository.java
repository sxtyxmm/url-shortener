package com.urlefy.repository;

import com.urlefy.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UrlRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortCode(String shortCode);
}
