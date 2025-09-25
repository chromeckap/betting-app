package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.group.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused") // used via Spring EL in @PreAuthorize
public class GroupMembershipPermission {
    private final GroupService groupService;
    private final GroupMembershipRepository groupMembershipRepository;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isGroupAdmin(Long groupId) {
        String userId = this.getCurrentUserId();
        Group group = groupService.findGroupById(groupId);

        return groupMembershipRepository.findByGroupIdAndCreatedBy(group.getId(), userId)
                .filter(membership -> membership.getRole() == GroupRole.ADMIN)
                .isPresent();
    }

    public boolean isGroupMember(Long groupId) {
        String userId = this.getCurrentUserId();
        Group group = groupService.findGroupById(groupId);

        return groupMembershipRepository.existsByGroupAndCreatedBy(group, userId);
    }

    public boolean isNotGroupMember(String inviteCode) {
        String userId = this.getCurrentUserId();
        return !groupMembershipRepository.existsByGroup_InviteCodeAndCreatedBy(inviteCode, userId);
    }

    public boolean canRemoveUserOrSelf(Long groupId, String targetUserId) {
        String userId = this.getCurrentUserId();

        if (userId.equals(targetUserId)) return true;

        return this.isGroupAdmin(groupId);
    }
}
