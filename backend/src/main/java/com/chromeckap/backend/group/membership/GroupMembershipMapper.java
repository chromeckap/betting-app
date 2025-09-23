package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMembershipMapper {

    public GroupMembership toEntity(Group group, String userId) {
        return GroupMembership.builder()
                .group(group)
                .createdBy(userId)
                .build();
    }
}
