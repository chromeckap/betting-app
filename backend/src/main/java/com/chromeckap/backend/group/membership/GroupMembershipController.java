package com.chromeckap.backend.group.membership;

import com.chromeckap.backend.group.GroupRole;
import com.chromeckap.backend.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipController {
    private final GroupMembershipService groupMembershipService;

    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<String>> getUsersInGroup(
            @PathVariable final Long groupId
    ) {
        log.info("Fetching group users with id {}", groupId);
        List<String> response = groupMembershipService.getUsersInGroup(groupId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{code}/join")
    public ResponseEntity<Void> joinGroupByCode(
            @PathVariable final String code
    ) {
        log.info("Joining group by code {}", code);
        groupMembershipService.joinGroupByCode(code);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{groupId}/users/{userId}/role")
    public ResponseEntity<Void> updateUserRoleInGroup(
            @PathVariable final Long groupId,
            @PathVariable final Long userId,
            @Valid @RequestBody final GroupRole role
            ) {
        log.info("Updating user with id {} in group with id {} with role {}", userId, groupId, role);
        groupMembershipService.updateUserRoleInGroup(groupId, userId, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(
            @PathVariable final Long groupId,
            @PathVariable final Long userId
    ) {
        log.info("Removing user with id {} from group with id {}", userId, groupId);
        groupMembershipService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
