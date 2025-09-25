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
    private final GroupMembershipValidator groupMembershipValidator;

    public GroupMembership findGroupMembershipByGroupIdAndCreatedBy(Long groupId, String userId) {
        return groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, userId)
                .orElseThrow(() -> new GroupNotFoundException("Group was not found."));
    }

    /**
     * Retrieve a list of usernames in a group.
     *
     * @param groupId the group id
     * @return list of usernames
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@groupMembershipPermission.isGroupMember(#groupId)")
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
    @PreAuthorize("@groupMembershipPermission.isNotGroupMember(#code)")
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
    @PreAuthorize("@groupMembershipPermission.isGroupAdmin(#groupId)")
    public void updateUserRoleInGroup(final Long groupId, final String userId, @Valid final GroupRole role) {
        log.debug("Updating role for user {} in group {} to {}", userId, groupId, role);

        groupMembershipValidator.validateRoleChange(groupId, userId, role);

        GroupMembership membership = this.findGroupMembershipByGroupIdAndCreatedBy(groupId, userId)
                .withRole(role);
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
    @PreAuthorize("@groupMembershipPermission.canRemoveUserOrSelf(#groupId, #userId)")
    public void removeUserFromGroup(final Long groupId, final String userId) {
        log.debug("Removing user {} from group {}", userId, groupId);

        groupMembershipValidator.validateUserRemoval(groupId, userId);

        GroupMembership membership = this.findGroupMembershipByGroupIdAndCreatedBy(groupId, userId);
        groupMembershipRepository.delete(membership);

        log.info("User {} removed from group {}", userId, groupId);
    }

    /**
     * Assigns the ADMIN role to the creator of a group.
     * <p>
     * This method is intended to be called after a new group has been created.
     * It creates a new {@link GroupMembership} for the given user and group,
     * sets the role to {@link GroupRole#ADMIN}, and persists it in the repository.
     *
     * @param group  the group for which the membership should be created
     * @param userId the ID of the user to be assigned as ADMIN
     */
    @Transactional
    public void createInitialAdminMembership(Group group, String userId) {
        GroupMembership membership = groupMembershipMapper.toEntity(group)
                .withRole(GroupRole.ADMIN)
                .withCreatedBy(userId);

        groupMembershipRepository.save(membership);
        log.info("Successfully assigned ADMIN role to user {} for group {}", userId, group.getId());
    }
}
