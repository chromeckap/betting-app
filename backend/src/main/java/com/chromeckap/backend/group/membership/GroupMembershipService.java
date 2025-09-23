package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.common.AuditingEntity;
import com.chromeckap.backend.exception.GroupNotFoundException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRepository;
import com.chromeckap.backend.group.GroupRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipService {
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
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
                .map(GroupMembership::getCreatedBy)
                .toList();
    }

    /**
     * Join a group using an invitation code.
     *
     * @param code the invite code
     */
    @Transactional
    public void joinGroupByCode(final String code, Authentication connectedUser) {
        log.debug("User attempting to join group with code {}", code);

        Group group = groupRepository.findByInviteCodeEquals(code)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        groupMembershipValidator.requireUserNotGroupMember(group, connectedUser);

        GroupMembership membership = groupMembershipMapper.toEntity(group)
                .withCreatedBy(connectedUser.getName());

        groupMembershipRepository.save(membership);

        log.info("User {} joined group {}", connectedUser.getName(), group.getId());
    }

    /**
     * Update a user's role in a group.
     *
     * @param groupId the group id
     * @param connectedUser  the connected user
     * @param role    the new role
     */
    @Transactional
    public void updateUserRoleInGroup(final Long groupId, final Authentication connectedUser, @Valid final GroupRole role) {
        log.debug("Updating role for user {} in group {} to {}", connectedUser.getName(), groupId, role);

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, connectedUser.getName())
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        groupMembershipValidator.requireUserIsGroupAdmin(membership.getGroup().getId(), connectedUser);

        membership.setRole(role);
        groupMembershipRepository.save(membership);

        log.info("Updated role for user {} in group {} to {}", connectedUser.getName(), groupId, role);
    }

    /**
     * Remove a user from a group.
     *
     * @param groupId the group id
     * @param userId the id of the user to be deleted
     * @param connectedUser  the connected user
     */
    @Transactional
    public void removeUserFromGroup(final Long groupId, final String userId, final Authentication connectedUser) {
        log.debug("Removing user {} from group {}", connectedUser.getName(), groupId);

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, connectedUser.getName())
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        boolean isSelfRemoving = connectedUser.getName().equals(userId);
        if (!isSelfRemoving) {
            groupMembershipValidator.requireUserIsGroupAdmin(membership.getGroup().getId(), connectedUser);
        }

        groupMembershipRepository.delete(membership);
        log.info("User {} removed from group {}", connectedUser.getName(), groupId);
    }
}
