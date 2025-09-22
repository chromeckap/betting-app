package com.chromeckap.backend.bet;

import com.chromeckap.backend.bet.option.BetOptionRequest;
import com.chromeckap.backend.utils.ValidDeadline;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public record BetRequest(
        String name,
        String description,
        @ValidDeadline
        LocalDateTime deadline,
        BetType type,
        List<@Valid BetOptionRequest> options
) {}
