package com.chromeckap.backend.bet;

import com.chromeckap.backend.bet.option.BetOptionRequest;
import com.chromeckap.backend.utils.ValidDeadline;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record BetRequest(
        @NotBlank(message = "{bet.name.required}")
        String name,

        @Size(max = 500, message = "{bet.description.size}")
        String description,

        @NotNull(message = "{bet.deadline.required}")
        @ValidDeadline
        LocalDateTime deadline,

        @NotNull(message = "{bet.type.required}")
        BetType type,

        @NotNull(message = "{bet.options.required}")
        @Size(min = 2, max = 20, message = "{bet.options.size}")
        List<@Valid BetOptionRequest> options
) {}

