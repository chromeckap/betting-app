package com.chromeckap.backend.exception;

public class LastAdminRemovalException extends RuntimeException {
    public LastAdminRemovalException(String message) {
        super(message);
    }

    public LastAdminRemovalException() {
        super("Cannot remove the last admin. Promote another member first");
    }
}
