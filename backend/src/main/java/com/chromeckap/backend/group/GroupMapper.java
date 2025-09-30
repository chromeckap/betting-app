package com.chromeckap.backend.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GroupMapper {

    public Group toEntity( GroupRequest request) {
        return Group.builder()
                .name(request.name())
                .build();
    }

    public GroupResponse toResponse(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .inviteCode(group.getInviteCode())
                .build();
    }

    public Group updateEntityAttributes(Group group, GroupRequest request) {
        return group
                .withName(request.name());
    }
}
