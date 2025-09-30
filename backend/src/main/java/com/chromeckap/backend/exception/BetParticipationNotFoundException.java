package com.chromeckap.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BetParticipationNotFoundException extends RuntimeException {
    private final Long betId;
    private final String userId;
}
