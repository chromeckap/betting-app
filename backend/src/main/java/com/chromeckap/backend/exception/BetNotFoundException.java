package com.chromeckap.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BetNotFoundException extends RuntimeException {
    private final Long betId;
}
