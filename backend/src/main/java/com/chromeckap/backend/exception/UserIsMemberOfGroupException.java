package com.chromeckap.backend.exception;

public class UserIsMemberOfGroupException extends RuntimeException {
    public UserIsMemberOfGroupException(String message) {
        super(message);
    }
}
