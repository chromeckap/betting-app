package com.chromeckap.backend.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException() {
        super("Group was not found");
    }

    public GroupNotFoundException(String message) {
        super(message);
    }
}
