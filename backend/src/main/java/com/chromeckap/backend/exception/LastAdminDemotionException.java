package com.chromeckap.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LastAdminDemotionException extends RuntimeException {
    private final Long groupId;
    private final String userId;
}
