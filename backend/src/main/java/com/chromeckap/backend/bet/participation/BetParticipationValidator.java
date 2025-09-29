package com.chromeckap.backend.bet.participation;

import com.chromeckap.backend.bet.Bet;
import com.chromeckap.backend.bet.BetStatus;
import com.chromeckap.backend.exception.BetClosedException;
import org.springframework.stereotype.Component;

@Component
public class BetParticipationValidator {

    public void validateBetOpen(Bet bet) {
        if (bet.isResolved() || bet.getStatus() != BetStatus.OPEN)
            throw new BetClosedException("Bet with id " + bet.getId() + " is not open for participation.");
    }

}
