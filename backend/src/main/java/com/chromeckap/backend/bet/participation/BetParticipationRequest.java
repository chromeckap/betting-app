package com.chromeckap.backend.bet.participation;

import jakarta.validation.constraints.NotNull;

public record BetParticipationRequest(
        @NotNull(message = "{bet.participation.optionId.required}")
        Long optionId
) {
}
