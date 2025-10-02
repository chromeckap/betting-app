package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.GroupMembershipNotFoundException;
import com.chromeckap.backend.exception.GroupNotFoundException;
import com.chromeckap.backend.exception.UserNotAuthenticatedException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRepository;
import com.chromeckap.backend.group.GroupRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipService {
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipMapper groupMembershipMapper;
    private final GroupMembershipPolicy groupMembershipPolicy;

    /**
     * Finds a group membership by group ID and user ID.
     *
     * @param groupId the group ID
     * @param userId  the user ID
     * @return the found {@link GroupMembership}
     * @throws GroupMembershipNotFoundException if the membership does not exist
     */
    public GroupMembership findGroupMembershipByGroupIdAndCreatedBy(Long groupId, String userId) {
        return groupMembershipRepository.findByGroupIdAndCreatedBy(groupId, userId)
                .orElseThrow(() -> new GroupMembershipNotFoundException(groupId, userId));
    }

    /**
     * Retrieves all group memberships for the given user.
     *
     * @param userId the unique identifier of the user
     * @return a list of {@link GroupMembership} entities where the user is a member
     */
    public List<GroupMembership> findAllGroupMembershipsByUserId(String userId) {
        return groupMembershipRepository.findAllByCreatedBy(userId);
    }

    /**
     * Retrieves all usernames of members in a given group.
     * Requires the current user to be a member of the group.
     *
     * @param groupId the group ID
     * @return list of usernames in the group
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@groupMembershipPermission.isGroupMember(#groupId)")
    public List<String> getUsersInGroup(final Long groupId) {
        log.debug("Fetching users in group with id {}", groupId);

        List<GroupMembership> memberships = groupMembershipRepository.findAllByGroupId(groupId);
        return memberships.stream()
                .map(GroupMembership::getCreatedBy)
                .toList();
    }

    /**
     * Allows the currently authenticated user to join a group using an invitation code.
     * Requires that the user is not already a member of the group.
     *
     * @param code the invitation code
     * @throws GroupNotFoundException         if no group matches the given code
     * @throws UserNotAuthenticatedException  if the user is not authenticated
     */
    @Transactional
    @PreAuthorize("@groupMembershipPermission.isNotGroupMember(#code)")
    public void joinGroupByCode(final String code) {
        log.debug("User attempting to join group with code {}", code);

        Group group = groupRepository.findByInviteCode(code)
                .orElseThrow(() -> new GroupNotFoundException(code));
        String userId = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElseThrow(UserNotAuthenticatedException::new);

        GroupMembership membership = groupMembershipMapper.toEntity(group)
                .withCreatedBy(userId);

        groupMembershipRepository.save(membership);

        log.info("User {} joined group {}", userId, group.getId());
    }

    /**
     * Updates a user's role in a group.
     * Only accessible to group admins.
     *
     * @param groupId the group ID
     * @param userId  the user ID whose role will be updated
     * @param role    the new role to assign
     * @throws GroupMembershipNotFoundException if the membership does not exist
     */
    @Transactional
    @PreAuthorize("@groupMembershipPermission.isGroupAdmin(#groupId)")
    public void updateUserRoleInGroup(final Long groupId, final String userId, final GroupRole role) {
        log.debug("Updating role for user {} in group {} to {}", userId, groupId, role);

        groupMembershipPolicy.assertRoleChangeable(groupId, userId, role);

        GroupMembership membership = this.findGroupMembershipByGroupIdAndCreatedBy(groupId, userId)
                .withRole(role);
        groupMembershipRepository.save(membership);

        log.info("Updated role for user {} in group {} to {}", userId, groupId, role);
    }

    /**
     * Removes a user from a group.
     * Can be executed by the user themselves or a group admin.
     *
     * @param groupId the group ID
     * @param userId  the ID of the user to remove
     * @throws GroupMembershipNotFoundException if the membership does not exist
     */
    @Transactional
    @PreAuthorize("@groupMembershipPermission.canRemoveUserOrSelf(#groupId, #userId)")
    public void removeUserFromGroup(final Long groupId, final String userId) {
        log.debug("Removing user {} from group {}", userId, groupId);

        groupMembershipPolicy.assertUserRemovable(groupId, userId);

        GroupMembership membership = this.findGroupMembershipByGroupIdAndCreatedBy(groupId, userId);
        groupMembershipRepository.delete(membership);

        log.info("User {} removed from group {}", userId, groupId);
    }

    /**
     * Assigns the ADMIN role to the creator of a group.
     * This method is intended to be called after a new group is created.
     * If the user is already a member, the method will skip creation.
     *
     * @param group  the group for which the membership is created
     * @param userId the ID of the user to assign as ADMIN
     */
    @Transactional
    public void createInitialAdminMembership(final Group group, final String userId) {
        GroupMembership membership = groupMembershipMapper.toEntity(group)
                .withRole(GroupRole.ADMIN)
                .withCreatedBy(userId);

        if (groupMembershipRepository.findByGroupIdAndCreatedBy(group.getId(), userId)
                .isPresent()
        ) {
            log.warn("Skipping initial admin creation: user {} is already a member of group {}", userId, group.getId());
            return;
        }

        groupMembershipRepository.save(membership);
        log.info("Successfully assigned ADMIN role to user {} for group {}", userId, group.getId());
    }
}
