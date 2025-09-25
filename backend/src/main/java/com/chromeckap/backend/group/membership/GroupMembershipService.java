package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.GroupNotFoundException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRepository;
import com.chromeckap.backend.group.GroupRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Retrieve a list of usernames in a group.
     *
     * @param groupId the group id
     * @return list of usernames
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@groupMembershipValidator.isGroupMember(#groupId)")
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
    @PreAuthorize("@groupMembershipValidator.isNotGroupMember(#code)")
    public void joinGroupByCode(final String code) {
        log.debug("User attempting to join group with code {}", code);

        Group group = groupRepository.findByInviteCodeEquals(code)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        GroupMembership membership = groupMembershipMapper.toEntity(group)
                .withCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        groupMembershipRepository.save(membership);

        log.info("User {} joined group {}", SecurityContextHolder.getContext().getAuthentication().getName(), group.getId());
    }

    /**
     * Update a user's role in a group.
     *
     * @param groupId the group id
     * @param role    the new role
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#groupId)")
    public void updateUserRoleInGroup(final Long groupId, final String userId, @Valid final GroupRole role) {
        log.debug("Updating role for user {} in group {} to {}", userId, groupId, role);

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, userId)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        membership.setRole(role);
        groupMembershipRepository.save(membership);

        log.info("Updated role for user {} in group {} to {}", userId, groupId, role);
    }

    /**
     * Remove a user from a group.
     *
     * @param groupId the group id
     * @param userId the id of the user to be deleted
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.canRemoveUserOrSelf(#groupId, #userId)")
    public void removeUserFromGroup(final Long groupId, final String userId) {
        log.debug("Removing user {} from group {}", userId, groupId);

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, userId)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));

        groupMembershipRepository.delete(membership);
        log.info("User {} removed from group {}", userId, groupId);
    }
}
