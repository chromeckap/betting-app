package com.chromeckap.backend.group;

import lombok.Builder;

@Builder
public record GroupResponse(
        String name,
        String inviteCode
) {}
