package com.chromeckap.backend.exception;

import com.chromeckap.backend.bet.BetStatus;

public class BetNotResolvableException extends RuntimeException {
    public BetNotResolvableException(String message) {
        super(message);
    }

    public BetNotResolvableException(Long betId, BetStatus status) {
        super("Bet " + betId + " cannot be resolved because it is " + status);
    }
}
