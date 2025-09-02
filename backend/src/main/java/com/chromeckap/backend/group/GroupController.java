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

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createGroup(
            @Valid @RequestBody final GroupRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGroup(
            @PathVariable final Long id,
            @Valid @RequestBody final GroupRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable final Long id
    ) {
        return ResponseEntity.noContent().build();
    }

}
