package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.UserHasNoAdminPermissionException;
import com.chromeckap.backend.exception.UserIsMemberOfGroupException;
import com.chromeckap.backend.exception.UserIsNotMemberOfGroupException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipValidator {
    private final GroupMembershipRepository groupMembershipRepository;

    public void validateAdminPermission(User user, Group group) {
        groupMembershipRepository.findByGroupIdAndCreatedById(user.getId(), group.getId())
                .filter(groupMembership -> groupMembership.getRole() == GroupRole.ADMIN)
                .orElseThrow(UserHasNoAdminPermissionException::new);
    }

    public void validateUserNotMemberOfGroup(User currentUser, Group group) {
        boolean isUserMemberOfGroup = groupMembershipRepository.existsByGroupAndCreatedBy(group, currentUser);
        if (isUserMemberOfGroup)
            throw new UserIsMemberOfGroupException("Group was not found.");
    }

    public void validateUserIsMemberOfGroup(User currentUser, Group group) {
        boolean isUserMemberOfGroup = groupMembershipRepository.existsByGroupAndCreatedBy(group, currentUser);
        if (!isUserMemberOfGroup)
            throw new UserIsNotMemberOfGroupException("Group was not found.");
    }
}
