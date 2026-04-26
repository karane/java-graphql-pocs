package org.karane.graphql.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateBookInput(
        @Size(max = 200) String title,
        @Size(max = 100) String authorName,
        @Min(1) @Max(5000) Integer pages
) {}
