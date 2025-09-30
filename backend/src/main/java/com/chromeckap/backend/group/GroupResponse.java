package com.chromeckap.backend.group;

import lombok.Builder;

@Builder
public record GroupResponse(
        Long id,
        String name,
        String inviteCode
) {}
