package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.exception.LastAdminDemotionException;
import com.chromeckap.backend.exception.LastAdminRemovalException;
import com.chromeckap.backend.exception.LastMemberRemovalException;
import com.chromeckap.backend.group.GroupRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipValidator {
    private final GroupMembershipRepository repository;

    public void validateUserRemoval(Long groupId, String userId) {
        List<GroupMembership> members = repository.findAllByGroupId(groupId);

        if (this.isLastMember(members, userId))
            throw new LastMemberRemovalException(groupId, userId);

        if (this.isLastAdmin(members, userId))
            throw new LastAdminRemovalException(groupId, userId);

        log.debug("User {} can be removed from group {}", userId, groupId);
    }

    public void validateRoleChange(Long groupId, String userId, GroupRole newRole) {
        if (newRole == GroupRole.ADMIN) return;
        List<GroupMembership> members = repository.findAllByGroupId(groupId);

        if (this.isLastAdmin(members, userId))
            throw new LastAdminDemotionException(groupId, userId);
    }

    private boolean isLastMember(List<GroupMembership> members, String userId) {
        return members.size() == 1 && members.getFirst().getCreatedBy().equals(userId);
    }

    private boolean isLastAdmin(List<GroupMembership> members, String userId) {
        GroupMembership membership = this.getMembership(members, userId);
        return membership != null
                && membership.getRole() == GroupRole.ADMIN
                && this.countAdmins(members) == 1;
    }

    private GroupMembership getMembership(List<GroupMembership> members, String userId) {
        return members.stream()
                .filter(m -> m.getCreatedBy().equals(userId))
                .findFirst()
                .orElse(null);
    }

    private long countAdmins(List<GroupMembership> members) {
        return members.stream()
                .filter(m -> m.getRole() == GroupRole.ADMIN)
                .count();
    }
}
