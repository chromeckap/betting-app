package com.chromeckap.backend.exception;

public class LastAdminDemotionException extends RuntimeException {
    public LastAdminDemotionException(String message) {
        super(message);
    }

    public LastAdminDemotionException() {
        super("Cannot demote the last admin. Promote another member first or delete the group");
    }
}
