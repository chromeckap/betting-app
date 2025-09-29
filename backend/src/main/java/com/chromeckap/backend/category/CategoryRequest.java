package com.chromeckap.backend.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryRequest(
        @NotBlank(message = "{category.name.required}")
        @Pattern(
                regexp = "^[\\p{L}0-9 ]+$",
                message = "{category.name.invalid}"
        )
        String name
) {}
