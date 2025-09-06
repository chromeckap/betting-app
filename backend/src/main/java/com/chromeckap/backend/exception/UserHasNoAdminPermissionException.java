package com.chromeckap.backend.exception;

public class UserHasNoAdminPermissionException extends RuntimeException {
    public UserHasNoAdminPermissionException() {
        super("User does not have admin permission.");
    }

    public UserHasNoAdminPermissionException(String message) {
        super(message);
    }
}
