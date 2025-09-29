package com.chromeckap.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BetOptionNotFoundException extends RuntimeException {
    private final Long optionId;
}
