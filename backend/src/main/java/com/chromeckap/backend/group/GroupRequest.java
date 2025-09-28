package com.chromeckap.backend.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GroupRequest(
        @NotBlank(message = "{group.name.required}")
        @Pattern(
                regexp = "^[\\p{L}0-9 ]+$",
                message = "{group.name.invalid}"
        )
        String name
) {}
