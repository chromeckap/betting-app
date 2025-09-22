package com.chromeckap.backend.bet;

import jakarta.validation.Valid;
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
                .name(bet.getName())
                .description(bet.getDescription())
                .deadline(bet.getDeadline())
                .isResolved(bet.isResolved())
                .status(bet.getStatus())
                .type(bet.getType())
                .build();
    }

    public Bet updateEntityAttributes(Bet bet, @Valid BetRequest request) {
        bet.setName(request.name());
        bet.setDescription(request.description());
        bet.setDeadline(request.deadline());
        return bet;
    }

}
