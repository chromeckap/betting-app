package com.chromeckap.backend.group;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GroupMapper {

    public Group toEntity(@Valid GroupRequest request) {
        return Group.builder()
                .name(request.name())
                .build();
    }

    public GroupResponse toResponse(Group group) {
        return GroupResponse.builder()
                .name(group.getName())
                .inviteCode(group.getInviteCode())
                .build();
    }

    public Group updateEntityAttributes(Group group, @Valid GroupRequest request) {
        return group
                .withName(request.name());
    }
}
