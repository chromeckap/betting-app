package com.chromeckap.backend.exception;

import lombok.Getter;

@Getter
public class GroupNotFoundException extends RuntimeException {
    private final Long groupId;
    private final String inviteCode;

    public GroupNotFoundException(Long groupId) {
        this.groupId = groupId;
        this.inviteCode = null;
    }

    public GroupNotFoundException(String inviteCode) {
        this.groupId = null;
        this.inviteCode = inviteCode;
    }
}
