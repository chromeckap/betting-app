package com.chromeckap.backend.group.membership;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupMembershipController {

    @GetMapping("/{groupId}/users")
    public ResponseEntity<Void> getUsersInGroup(
            @PathVariable final Long groupId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{code}/join")
    public ResponseEntity<Void> joinGroupByCode(
            @PathVariable final String code
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{groupId}/users/{userId}/role")
    public ResponseEntity<Void> updateUserRoleInGroup(
            @PathVariable final Long groupId,
            @PathVariable final Long userId
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(
            @PathVariable final Long groupId,
            @PathVariable final Long userId
    ) {
        return ResponseEntity.noContent().build();
    }
}
