package com.chromeckap.backend.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoriesInGroup(
            @PathVariable final Long groupId,
            Authentication connectedUser
    ) {
        log.info("Fetching categories in group with id {}", groupId);
        List<CategoryResponse> response = categoryService.getCategoriesInGroup(groupId, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(
            @PathVariable final Long groupId,
            @Valid @RequestBody final CategoryRequest request,
            Authentication connectedUser
    ) {
        log.info("Creating category {} for group with id {}", request, groupId);
        Long response = categoryService.createCategory(groupId, request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id,
            @Valid @RequestBody final CategoryRequest request,
            Authentication connectedUser
    ) {
        log.info("Updating category with id {} with body {}", id, request);
        Long response = categoryService.updateCategory(groupId, id, request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id,
            Authentication connectedUser
    ) {
        log.info("Deleting category with id {}", id);
        categoryService.deleteCategory(groupId, id, connectedUser);
        return ResponseEntity.noContent().build();
    }
}
