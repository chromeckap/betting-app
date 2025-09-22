package com.chromeckap.backend.bet;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BetResponse(
        String name,
        String description,
        LocalDateTime deadline,
        boolean isResolved,
        BetStatus status,
        BetType type
) {
}
