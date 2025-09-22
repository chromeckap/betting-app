package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.GroupNotFoundException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRepository;
import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.user.User;
import com.chromeckap.backend.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipService {
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMembershipMapper groupMembershipMapper;
    private final GroupMembershipValidator groupMembershipValidator;

    /**
     * Retrieve a list of usernames in a group.
     *
     * @param groupId the group id
     * @return list of usernames
     */
    @Transactional(readOnly = true)
    public List<String> getUsersInGroup(final Long groupId) {
        log.debug("Fetching users in group with id {}", groupId);

        List<GroupMembership> memberships = groupMembershipRepository.findByGroupId(groupId);
        return memberships.stream()
                .map(m -> m.getCreatedBy().getUsername())
                .toList();
    }

    /**
     * Join a group using an invitation code.
     *
     * @param code the invite code
     */
    @Transactional
    public void joinGroupByCode(final String code) {
        log.debug("User attempting to join group with code {}", code);

        User currentUser = userService.getCurrentUser();
        Group group = groupRepository.findByInviteCodeEquals(code)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        groupMembershipValidator.requireUserNotGroupMember(group);

        GroupMembership membership = groupMembershipMapper.toEntity(group, currentUser);
        groupMembershipRepository.save(membership);

        log.info("User {} joined group {}", currentUser.getUsername(), group.getId());
    }

    /**
     * Update a user's role in a group.
     *
     * @param groupId the group id
     * @param userId  the user id
     * @param role    the new role
     */
    @Transactional
    public void updateUserRoleInGroup(final Long groupId, final Long userId, @Valid final GroupRole role) {
        log.debug("Updating role for user {} in group {} to {}", userId, groupId, role);

        User currentUser = userService.getCurrentUser();
        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedById(groupId, userId)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        groupMembershipValidator.requireUserIsGroupAdmin(membership.getGroup().getId());

        membership.setRole(role);
        groupMembershipRepository.save(membership);

        log.info("Updated role for user {} in group {} to {}", userId, groupId, role);
    }

    /**
     * Remove a user from a group.
     *
     * @param groupId the group id
     * @param userId  the user id
     */
    @Transactional
    public void removeUserFromGroup(final Long groupId, final Long userId) {
        log.debug("Removing user {} from group {}", userId, groupId);

        User currentUser = userService.getCurrentUser();
        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedById(groupId, userId)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        boolean isSelfRemoving = currentUser.getId().equals(userId);
        if (!isSelfRemoving) {
            groupMembershipValidator.requireUserIsGroupAdmin(membership.getGroup().getId());
        }

        groupMembershipRepository.delete(membership);
        log.info("User {} removed from group {}", userId, groupId);
    }
}
