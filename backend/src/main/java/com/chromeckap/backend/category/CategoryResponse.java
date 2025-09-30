package com.chromeckap.backend.category;

import lombok.Builder;

@Builder
public record CategoryResponse(
        Long id,
        String name
) {}
