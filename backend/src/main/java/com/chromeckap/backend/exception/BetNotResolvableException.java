package com.chromeckap.backend.exception;

import com.chromeckap.backend.bet.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BetNotResolvableException extends RuntimeException {
    private final Long betId;
    private final BetStatus betStatus;
}
