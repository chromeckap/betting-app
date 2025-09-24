package com.chromeckap.backend.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
            @PathVariable final Long groupId
    ) {
        log.info("Fetching categories in group with id {}", groupId);
        List<CategoryResponse> response = categoryService.getCategoriesInGroup(groupId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(
            @PathVariable final Long groupId,
            @Valid @RequestBody final CategoryRequest request
    ) {
        log.info("Creating category {} for group with id {}", request, groupId);
        Long response = categoryService.createCategory(groupId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id,
            @Valid @RequestBody final CategoryRequest request
    ) {
        log.info("Updating category with id {} with body {}", id, request);
        Long response = categoryService.updateCategory(groupId, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id
    ) {
        log.info("Deleting category with id {}", id);
        categoryService.deleteCategory(groupId, id);
        return ResponseEntity.noContent().build();
    }
}
