package com.chromeckap.backend.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    @GetMapping("/groups/{groupId}/categories")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByGroupId(
            @PathVariable final Long groupId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/groups/{groupId}/categories")
    public ResponseEntity<Void> createCategory(
            @PathVariable final Long groupId,
            @Valid @RequestBody final CategoryRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable final Long id,
            @Valid @RequestBody final CategoryRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable final Long id
    ) {
        return ResponseEntity.ok().build();
    }
}
