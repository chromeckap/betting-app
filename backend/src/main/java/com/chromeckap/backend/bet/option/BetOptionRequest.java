package com.chromeckap.backend.bet.option;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BetOptionRequest(
        Long id,
        @NotBlank(message = "{bet.option.name.required}")
        @Size(max = 100, message = "{bet.option.name.size}")
        String name
) {
}
