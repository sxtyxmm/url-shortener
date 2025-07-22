package com.urlefy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class MongoIndexConfig implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        // Create index on shortCode for fast lookups
        mongoTemplate.indexOps("shortUrl")
                .ensureIndex(new Index().on("shortCode", Sort.Direction.ASC).unique());
        
        // Create index on clickCount for analytics
        mongoTemplate.indexOps("shortUrl")
                .ensureIndex(new Index().on("clickCount", Sort.Direction.DESC));
        
        // Create index on expiryDate for cleanup operations
        mongoTemplate.indexOps("shortUrl")
                .ensureIndex(new Index().on("expiryDate", Sort.Direction.ASC));
        
        // Create compound index for active popular URLs
        mongoTemplate.indexOps("shortUrl")
                .ensureIndex(new Index()
                    .on("expiryDate", Sort.Direction.ASC)
                    .on("clickCount", Sort.Direction.DESC));
    }
}
