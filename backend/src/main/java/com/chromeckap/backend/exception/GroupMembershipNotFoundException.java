package com.chromeckap.backend.exception;

public class GroupMembershipNotFoundException extends RuntimeException {
    public GroupMembershipNotFoundException(String message) {
        super(message);
    }

    public GroupMembershipNotFoundException() {
        super("No membership in group");
    }
}
