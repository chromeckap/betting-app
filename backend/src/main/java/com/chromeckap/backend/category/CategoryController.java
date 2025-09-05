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

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategoriesByGroupId(
            @PathVariable final Long groupId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @PathVariable final Long groupId,
            @Valid @RequestBody final CategoryRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id,
            @Valid @RequestBody final CategoryRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable final Long groupId,
            @PathVariable final Long id
    ) {
        return ResponseEntity.ok().build();
    }
}
