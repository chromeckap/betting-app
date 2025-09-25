package com.chromeckap.backend.bet.participation;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class BetParticipationMapper {

    public BetParticipation toEntity(@Valid final BetParticipationRequest request) {
        return BetParticipation.builder()
                .build();
    }
}
