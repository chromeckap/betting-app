package com.chromeckap.backend.category;

import com.chromeckap.backend.exception.CategoryNotFoundException;
import com.chromeckap.backend.group.Group;
import com.chromeckap.backend.group.GroupService;
import com.chromeckap.backend.group.membership.GroupMembershipValidator;
import com.chromeckap.backend.user.User;
import com.chromeckap.backend.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserService userService;
    private final GroupMembershipValidator groupMembershipValidator;

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
    public List<CategoryResponse> getCategoriesInGroup(final Long groupId) {
        log.debug("Getting categories in group with id {}", groupId);

        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();
        groupMembershipValidator.validateUserIsMemberOfGroup(user, group);

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
    public Long createCategory(final Long groupId, @Valid final CategoryRequest request) {
        log.debug("Creating category {} in group with id {}", request, groupId);

        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();
        groupMembershipValidator.validateAdminPermission(user, group);

        Category category = categoryMapper.toEntity(request);
        category.setGroup(group);

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
    public Long updateCategory(final Long groupId, final Long id, @Valid final CategoryRequest request) {
        log.debug("Updating category with id {} in group {} using {}", id, groupId, request);

        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();
        groupMembershipValidator.validateAdminPermission(user, group);

        Category category = this.findCategoryByIdAndGroupId(id, groupId);

        categoryMapper.updateEntityAttributes(category, request);
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
    public void deleteCategory(final Long groupId, final Long id) {
        log.debug("Deleting category with id {} from group {}", id, groupId);

        Group group = groupService.findGroupById(groupId);
        User user = userService.getCurrentUser();
        groupMembershipValidator.validateAdminPermission(user, group);

        Category category = this.findCategoryByIdAndGroupId(id, groupId);

        categoryRepository.delete(category);
        log.info("Successfully deleted category with id {} from group {}", id, groupId);
    }
}
