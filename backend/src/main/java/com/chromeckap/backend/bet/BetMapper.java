package com.chromeckap.backend.bet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BetMapper {

    public Bet toEntity(BetRequest request) {
        return Bet.builder()
                .name(request.name())
                .description(request.description())
                .deadline(request.deadline())
                .type(request.type())
                .build();
    }

    public BetResponse toResponse(Bet bet) {
        return BetResponse.builder()
                .id(bet.getId())
                .name(bet.getName())
                .description(bet.getDescription())
                .deadline(bet.getDeadline())
                .isResolved(bet.isResolved())
                .status(bet.getStatus())
                .type(bet.getType())
                .build();
    }

    public Bet updateEntityAttributes(Bet bet, BetRequest request) {
        return bet
                .withName(request.name())
                .withDescription(request.description())
                .withDeadline(request.deadline());
    }

}
