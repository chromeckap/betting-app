package com.chromeckap.backend.exception;

public class LastMemberRemovalException extends RuntimeException {
    public LastMemberRemovalException(String message) {
        super(message);
    }

    public LastMemberRemovalException() {
        super("Last player of the group cannot be removed, delete the group instead");
    }
}
