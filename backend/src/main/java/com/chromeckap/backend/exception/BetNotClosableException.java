package com.chromeckap.backend.exception;

import com.chromeckap.backend.bet.BetStatus;

public class BetNotClosableException extends RuntimeException {
    public BetNotClosableException(String message) {
        super(message);
    }

    public BetNotClosableException(Long betId, BetStatus status) {
        super("Bet " + betId + " cannot be closed because it is " + status);
    }
}
