package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.user.User;
import org.springframework.stereotype.Component;

@Component
public class GroupMembershipMapper {

    public GroupMembership toEntity(Group group, User user) {
        return GroupMembership.builder()
                .group(group)
                .createdBy(user)
                .build();
    }
}
