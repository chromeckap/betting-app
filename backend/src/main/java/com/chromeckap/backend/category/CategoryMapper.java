package com.chromeckap.backend.category;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryMapper {
    public Category toEntity(@Valid CategoryRequest request) {
        return Category.builder()
                .name(request.name())
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .name(category.getName())
                .build();
    }

    public Category updateEntityAttributes(Category category, @Valid CategoryRequest request) {
        category.setName(request.name());
        return category;
    }
}
