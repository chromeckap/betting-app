package com.chromeckap.backend.bet.participation;

import org.springframework.stereotype.Component;

@Component
public class BetParticipationMapper {

    public BetParticipation toEntity(final BetParticipationRequest request) {
        return BetParticipation.builder()
                .build();
    }
}
