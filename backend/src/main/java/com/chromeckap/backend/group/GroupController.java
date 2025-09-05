package com.chromeckap.backend.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(
            @PathVariable Long id
    ) {
        log.info("Fetching group with id {}", id);
        GroupResponse response = groupService.getGroupById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createGroup(
            @Valid @RequestBody final GroupRequest request
    ) {
        log.info("Creating group {}", request);
        groupService.createGroup(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGroup(
            @PathVariable final Long id,
            @Valid @RequestBody final GroupRequest request
    ) {
        log.info("Updating group {} with id {}", request, id);
        groupService.updateGroup(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable final Long id
    ) {
        log.info("Deleting group with id {}", id);
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

}
