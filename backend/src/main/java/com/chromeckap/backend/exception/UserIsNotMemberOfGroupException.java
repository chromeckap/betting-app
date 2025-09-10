package com.chromeckap.backend.exception;

public class UserIsNotMemberOfGroupException extends RuntimeException {
    public UserIsNotMemberOfGroupException(String message) {
        super(message);
    }
}
