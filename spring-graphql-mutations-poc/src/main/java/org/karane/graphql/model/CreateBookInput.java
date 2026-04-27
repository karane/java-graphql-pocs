package org.karane.graphql.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBookInput(
        
        @NotBlank 
        @Size(max = 200) 
        String title,
        
        @NotBlank 
        @Size(max = 100) 
        String authorName,
        
        @Min(1) 
        @Max(5000) 
        Integer pages
) {}
