package com.chromeckap.backend.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GroupRequest(
        @NotBlank(message = "Group name cannot be empty")
        @Pattern(
                regexp = "^[\\p{L}0-9 ]+$",
                message = "Group name can only contain letters, numbers, and spaces"
        )
        String name
) {}
