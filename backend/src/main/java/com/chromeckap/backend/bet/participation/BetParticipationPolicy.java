package com.chromeckap.backend.bet.participation;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.bet.BetStatus;
import com.chromeckap.backend.exception.AlreadyParticipatingException;
import com.chromeckap.backend.exception.BetClosedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BetParticipationPolicy {
    private final BetParticipationRepository betParticipationRepository;

    public void assertBetOpen(Bet bet) {
        if (bet.isResolved() || bet.getStatus() != BetStatus.OPEN)
            throw new BetClosedException(bet.getId());
    }

    public void assertUserNotParticipating(Long betId, String userId) {
        boolean isAlreadyParticipating = betParticipationRepository.existsByBetIdAndCreatedBy(betId, userId);
        if (isAlreadyParticipating)
            throw new AlreadyParticipatingException(betId, userId);
    }
}
