package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.UserHasNoAdminPermissionException;
import com.chromeckap.backend.exception.UserIsMemberOfGroupException;
import com.chromeckap.backend.exception.UserIsNotMemberOfGroupException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.group.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipValidator {
    private final GroupService groupService;
    private final GroupMembershipRepository groupMembershipRepository;

    public void requireUserIsGroupAdmin(Long groupId, Authentication connectedUser) {
        Group group = groupService.findGroupById(groupId);

        groupMembershipRepository.findByGroupIdAndCreatedBy(group.getId(), connectedUser.getName())
                .filter(membership -> membership.getRole() == GroupRole.ADMIN)
                .orElseThrow(UserHasNoAdminPermissionException::new);
    }

    public void requireUserIsGroupMember(Long groupId, Authentication connectedUser) {
        Group group = groupService.findGroupById(groupId);

        boolean isMember = groupMembershipRepository.existsByGroupAndCreatedBy(group, connectedUser.getName());
        if (!isMember)
            throw new UserIsNotMemberOfGroupException("User is not a member of this group.");
    }

    public void requireUserNotGroupMember(Group group, Authentication connectedUser) {
        boolean isMember = groupMembershipRepository.existsByGroupAndCreatedBy(group, connectedUser.getName());
        if (isMember)
            throw new UserIsMemberOfGroupException("User is a member of this group.");
    }
}
