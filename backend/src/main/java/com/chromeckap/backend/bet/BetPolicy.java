package com.chromeckap.backend.bet;

import com.chromeckap.backend.exception.BetNotClosableException;
import com.chromeckap.backend.exception.BetNotResolvableException;
import org.springframework.stereotype.Component;

@Component
public class BetPolicy {

    public void assertClosable(Bet bet) {
        if (bet.getStatus() != BetStatus.OPEN)
            throw new BetNotClosableException(bet.getId(), bet.getStatus());
    }

    public void assertResolvable(Bet bet) {
        if (bet.getStatus() == BetStatus.RESOLVED)
            throw new BetNotResolvableException(bet.getId(), bet.getStatus());
    }
}
