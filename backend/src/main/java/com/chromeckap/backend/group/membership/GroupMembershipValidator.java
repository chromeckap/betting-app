package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.UserHasNoAdminPermissionException;
import com.chromeckap.backend.exception.UserIsMemberOfGroupException;
import com.chromeckap.backend.exception.UserIsNotMemberOfGroupException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.group.GroupService;
import com.chromeckap.backend.user.User;
import com.chromeckap.backend.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipValidator {
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMembershipRepository groupMembershipRepository;

    public void requireUserIsGroupAdmin(Long groupId) {
        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();

        groupMembershipRepository.findByGroupIdAndCreatedById(group.getId(), user.getId())
                .filter(membership -> membership.getRole() == GroupRole.ADMIN)
                .orElseThrow(UserHasNoAdminPermissionException::new);
    }

    public void requireUserIsGroupMember(Long groupId) {
        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();

        boolean isMember = groupMembershipRepository.existsByGroupAndCreatedBy(group, user);
        if (!isMember)
            throw new UserIsNotMemberOfGroupException("User is not a member of this group.");
    }

    public void requireUserNotGroupMember(Group group) {
        User user = userService.getCurrentUser();

        boolean isMember = groupMembershipRepository.existsByGroupAndCreatedBy(group, user);
        if (isMember)
            throw new UserIsMemberOfGroupException("User is a member of this group.");
    }
}
