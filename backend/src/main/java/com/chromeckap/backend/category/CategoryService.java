package com.chromeckap.backend.category;

import com.chromeckap.backend.exception.CategoryNotFoundException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final GroupService groupService;

    /**
     * Helper method for finding a category by id and group id.
     *
     * @param id      category id
     * @param groupId group id
     * @return found {@link Category}
     * @throws CategoryNotFoundException if category is not found in the given group
     */
    @Transactional(readOnly = true)
    public Category findCategoryByIdAndGroupId(final Long id, final Long groupId) {
        return categoryRepository.findByIdAndGroupId(id, groupId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with id %s was not found in group %s.", id, groupId)
                ));
    }

    /**
     * Retrieve all categories in a given group.
     *
     * @param groupId the group id
     * @return list of {@link CategoryResponse}
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@groupMembershipValidator.isGroupMember(#groupId)")
    public List<CategoryResponse> getCategoriesInGroup(final Long groupId) {
        log.debug("Getting categories in group with id {}", groupId);

        List<CategoryResponse> responses = categoryRepository.findByGroupId(groupId).stream()
                .map(categoryMapper::toResponse)
                .toList();

        log.debug("Fetched {} categories in group with id {}", responses.size(), groupId);
        return responses;
    }

    /**
     * Create a new category inside a group.
     *
     * @param groupId group id
     * @param request category request
     * @return id of created category
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#groupId)")
    public Long createCategory(final Long groupId, @Valid final CategoryRequest request) {
        log.debug("Creating category {} in group with id {}", request, groupId);

        Group group = groupService.findGroupById(groupId);
        Category category = categoryMapper.toEntity(request)
                .withGroup(group)
                .withCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        Category savedCategory = categoryRepository.save(category);
        log.info("Successfully created category with id {} in group {}", savedCategory.getId(), groupId);

        return savedCategory.getId();
    }

    /**
     * Update an existing category in a group.
     *
     * @param groupId group id
     * @param id      category id
     * @param request category request
     * @return id of updated category
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#groupId)")
    public Long updateCategory(final Long groupId, final Long id, @Valid final CategoryRequest request) {
        log.debug("Updating category with id {} in group {} using {}", id, groupId, request);

        Category category = this.findCategoryByIdAndGroupId(id, groupId);
        category = categoryMapper.updateEntityAttributes(category, request);
        Category savedCategory = categoryRepository.save(category);

        log.info("Successfully updated category with id {} in group {}", savedCategory.getId(), groupId);
        return savedCategory.getId();
    }

    /**
     * Delete a category from a group.
     *
     * @param groupId group id
     * @param id      category id
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#groupId)")
    public void deleteCategory(final Long groupId, final Long id) {
        log.debug("Deleting category with id {} from group {}", id, groupId);

        Category category = this.findCategoryByIdAndGroupId(id, groupId);
        categoryRepository.delete(category);

        log.info("Successfully deleted category with id {} from group {}", id, groupId);
    }
}
