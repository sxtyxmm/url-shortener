package com.urlefy.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequest {
    
    @NotBlank(message = "URL cannot be empty")
    @URL(message = "Invalid URL format")
    @Size(max = 2048, message = "URL too long")
    private String url;
    
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Custom alias can only contain letters, numbers, hyphens, and underscores")
    @Size(min = 3, max = 12, message = "Custom alias must be between 3 and 12 characters")
    private String customAlias;
}
