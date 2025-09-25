package com.chromeckap.backend.group;

import com.chromeckap.backend.exception.GroupNotFoundException;
import com.chromeckap.backend.group.membership.GroupMembershipService;
import com.chromeckap.backend.utils.InviteCodeGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GroupMembershipService groupMembershipService;

    /**
     * Helper method for finding a group by id.
     *
     * @param id the unique identifier of the group
     * @return the found {@link Group}
     * @throws GroupNotFoundException if no group with given id exists
     */
    @Transactional(readOnly = true)
    public Group findGroupById(final Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(
                        String.format("Group with id %s was not found.", id)
                ));
    }

    /**
     * Retrieve a single group by its id.
     *
     * @param id the unique identifier of the group
     * @return mapped {@link GroupResponse}
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@groupMembershipValidator.isGroupMember(#id)")
    public GroupResponse getGroupById(final Long id) {
        log.debug("Getting group with id {}", id);

        Group group = this.findGroupById(id);
        return groupMapper.toResponse(group);
    }

    /**
     * Create a new group based on a request payload.
     *
     * @param request validated {@link GroupRequest}
     * @return the id of the newly created group
     */
    @Transactional
    public Long createGroup(@Valid final GroupRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("Creating group {}", request);

        Group group = groupMapper.toEntity(request)
                .withInviteCode(this.generateUniqueInviteCode())
                .withCreatedBy(userId);

        Group savedGroup = groupRepository.save(group);
        groupMembershipService.createInitialAdminMembership(group, userId);
        log.info("Successfully created group {}", savedGroup);

        return savedGroup.getId();
    }

    /**
     * Update attributes of an existing group.
     *
     * @param id      the id of the group to update
     * @param request validated {@link GroupRequest} with new values
     * @return the id of the updated group
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#id)")
    public Long updateGroup(final Long id, @Valid final GroupRequest request) {
        log.debug("Updating group {} with id {}", request, id);

        Group group = this.findGroupById(id);
        group = groupMapper.updateEntityAttributes(group, request);
        Group savedGroup = groupRepository.save(group);
        log.info("Successfully updated group {} with id {}", request, savedGroup);

        return savedGroup.getId();
    }

    /**
     * Delete a group by its id.
     *
     * @param id the id of the group to delete
     */
    @Transactional
    @PreAuthorize("@groupMembershipValidator.isGroupAdmin(#id)")
    public void deleteGroup(final Long id) {
        log.debug("Deleting group with id {}", id);

        Group group = this.findGroupById(id);
        groupRepository.delete(group);
        log.info("Successfully deleted group with id {}", id);
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            code = InviteCodeGenerator.generate();
        } while (groupRepository.existsByInviteCode(code));

        return code;
    }

}
