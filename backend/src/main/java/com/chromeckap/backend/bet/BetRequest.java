package com.chromeckap.backend.bet;

import com.chromeckap.backend.utils.ValidDeadline;

import java.time.LocalDateTime;

public record BetRequest(
        String title,
        String description,
        @ValidDeadline
        LocalDateTime deadline,
        BetType type,
        Long categoryId
) {}
